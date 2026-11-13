import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ApiService, CustomerFull, SessionDto, SpotDto, VehicleDto, VehicleTypeDto, errorMessage } from './api.service';

type View = 'login' | 'dashboard' | 'entry' | 'exit' | 'sessions' | 'history' | 'spots' | 'customers' | 'incidents' | 'detail';

/** Estancia tal como la muestra la interfaz (derivada de la respuesta de la API). */
interface Session {
  id: number;
  vehicleId: number;
  plate: string;
  vehicle: string;
  customer: string;
  spot: string;
  enteredAt: string;
  exitedAt: string | null;
  operator: string;
}

interface Incident {
  id: number;
  sessionId: number;
  plate: string;
  spot: string;
  category: string;
  description: string;
  createdAt: string;
}

const INCIDENTS_KEY = 'ac.incidents';
const normalizePlate = (value: string): string => value.replace(/[\s-]/g, '').toUpperCase();
const formatDate = (iso: string): string =>
  new Date(iso).toLocaleString('es-CO', { day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit' });

@Component({
  selector: 'ac-root',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  readonly operatorName = 'Carlos Gomez';
  readonly operatorInitials = 'CG';
  readonly today = new Date().toLocaleDateString('es-CO', { weekday: 'long', day: 'numeric', month: 'long' }).toUpperCase();

  readonly view = signal<View>('dashboard');
  readonly notice = signal('');
  readonly noticeIsError = signal(false);
  readonly loading = signal(false);
  readonly menuOpen = signal(false);
  readonly foundVehicle = signal<VehicleDto | null>(null);
  readonly vehicleLookupError = signal('');
  readonly entryComplete = signal(false);
  readonly exitSearched = signal(false);
  readonly selectedSession = signal<Session | null>(null);
  readonly incidentTarget = signal<Session | null>(null);
  readonly incidents = signal<Incident[]>(this.loadIncidents());

  /** Datos que provienen del backend (fuente de verdad: base de datos). */
  readonly sessions = signal<Session[]>([]);
  readonly history = signal<Session[]>([]);
  readonly spots = signal<SpotDto[]>([]);

  readonly availableSpots = computed(() => this.spots().filter((spot) => spot.available));
  readonly occupiedSpots = computed(() => this.spots().filter((spot) => !spot.available));
  /** Celdas libres compatibles con el tipo del vehículo encontrado. */
  readonly entrySpots = computed(() => {
    const typeId = this.foundVehicle()?.vehicleType?.id;
    return this.availableSpots().filter((spot) => !typeId || spot.vehicleType?.id === typeId);
  });
  readonly selectedSessionIncidents = computed(() => {
    const session = this.selectedSession();
    return session ? this.incidents().filter((incident) => incident.sessionId === session.id) : [];
  });

  readonly entryForm = new FormGroup({
    plate: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(5)] }),
    spot: new FormControl('', { nonNullable: true, validators: Validators.required }),
    observation: new FormControl('', { nonNullable: true })
  });
  readonly exitForm = new FormGroup({
    plate: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(5)] })
  });
  readonly incidentForm = new FormGroup({
    category: new FormControl('Observación', { nonNullable: true, validators: Validators.required }),
    description: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(500)] })
  });

  // Clientes y vehículos
  readonly customers = signal<CustomerFull[]>([]);
  readonly customerSearch = new FormControl('', { nonNullable: true });
  readonly customerDialog = signal<{ editing: CustomerFull | null } | null>(null);
  readonly vehiclesTarget = signal<CustomerFull | null>(null);
  readonly customerVehicles = signal<VehicleDto[]>([]);
  readonly vehicleTypes = signal<VehicleTypeDto[]>([]);
  readonly dialogError = signal('');
  readonly customerForm = new FormGroup({
    identificationType: new FormControl('CC', { nonNullable: true, validators: Validators.required }),
    identificationNumber: new FormControl('', { nonNullable: true, validators: Validators.required }),
    name: new FormControl('', { nonNullable: true, validators: Validators.required }),
    email: new FormControl('', { nonNullable: true, validators: Validators.email }),
    phone: new FormControl('', { nonNullable: true })
  });
  readonly vehicleForm = new FormGroup({
    licensePlate: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(5)] }),
    brand: new FormControl('', { nonNullable: true, validators: Validators.required }),
    model: new FormControl<number | null>(null, { validators: [Validators.required, Validators.min(1900)] }),
    color: new FormControl('', { nonNullable: true, validators: Validators.required }),
    vehicleTypeId: new FormControl<number | null>(null, { validators: Validators.required })
  });

  // Celdas
  readonly spotDialog = signal<{ editing: SpotDto | null } | null>(null);
  readonly spotForm = new FormGroup({
    spotNumber: new FormControl('', { nonNullable: true, validators: Validators.required }),
    vehicleTypeId: new FormControl<number | null>(null, { validators: Validators.required })
  });

  constructor(private readonly api: ApiService) {
    this.refresh();
    this.api.getVehicleTypes().subscribe({ next: (types) => this.vehicleTypes.set(types), error: (e: unknown) => this.fail(errorMessage(e)) });
  }

  loadCustomers(): void {
    this.api.getCustomers(this.customerSearch.value.trim()).subscribe({
      next: (page) => this.customers.set(page.content),
      error: (e: unknown) => this.fail(errorMessage(e))
    });
  }

  openCustomerDialog(customer: CustomerFull | null): void {
    this.dialogError.set('');
    this.customerForm.reset({
      identificationType: customer?.identificationType ?? 'CC',
      identificationNumber: customer?.identificationNumber ?? '',
      name: customer?.name ?? '',
      email: customer?.email ?? '',
      phone: customer?.phone ?? ''
    });
    this.customerDialog.set({ editing: customer });
  }

  saveCustomer(): void {
    const dialog = this.customerDialog();
    if (!dialog) return;
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }
    const v = this.customerForm.getRawValue();
    const body = {
      identificationType: v.identificationType,
      identificationNumber: v.identificationNumber.trim(),
      name: v.name.trim(),
      email: v.email.trim() || null,
      phone: v.phone.trim() || null
    };
    const request = dialog.editing ? this.api.updateCustomer(dialog.editing.id, body) : this.api.createCustomer(body);
    request.subscribe({
      next: (saved) => {
        this.customerDialog.set(null);
        this.loadCustomers();
        this.success(`Cliente ${saved.name} ${dialog.editing ? 'actualizado' : 'creado'}.`);
      },
      error: (e: unknown) => this.dialogError.set(errorMessage(e))
    });
  }

  deleteCustomer(customer: CustomerFull): void {
    if (!confirm(`¿Eliminar al cliente ${customer.name}?`)) return;
    this.api.deleteCustomer(customer.id).subscribe({
      next: () => { this.loadCustomers(); this.success(`Cliente ${customer.name} eliminado.`); },
      error: (e: unknown) => this.fail(errorMessage(e))
    });
  }

  openVehicles(customer: CustomerFull): void {
    this.dialogError.set('');
    this.customerVehicles.set([]);
    this.vehicleForm.reset({ licensePlate: '', brand: '', model: null, color: '', vehicleTypeId: null });
    this.vehiclesTarget.set(customer);
    this.loadCustomerVehicles(customer.id);
  }

  private loadCustomerVehicles(customerId: number): void {
    this.api.getVehiclesByCustomer(customerId).subscribe({
      next: (list) => this.customerVehicles.set(list),
      error: (e: unknown) => this.dialogError.set(errorMessage(e))
    });
  }

  saveVehicle(): void {
    const customer = this.vehiclesTarget();
    if (!customer) return;
    if (this.vehicleForm.invalid) {
      this.vehicleForm.markAllAsTouched();
      return;
    }
    const v = this.vehicleForm.getRawValue();
    this.api.createVehicle({
      licensePlate: normalizePlate(v.licensePlate),
      brand: v.brand.trim(),
      model: Number(v.model),
      color: v.color.trim(),
      customerId: customer.id,
      vehicleTypeId: Number(v.vehicleTypeId)
    }).subscribe({
      next: (vehicle) => {
        this.dialogError.set('');
        this.vehicleForm.reset({ licensePlate: '', brand: '', model: null, color: '', vehicleTypeId: null });
        this.loadCustomerVehicles(customer.id);
        this.success(`Vehículo ${vehicle.licensePlate} registrado para ${customer.name}.`);
      },
      error: (e: unknown) => this.dialogError.set(errorMessage(e))
    });
  }

  openSpotDialog(spot: SpotDto | null): void {
    this.dialogError.set('');
    this.spotForm.reset({ spotNumber: spot?.spotNumber ?? '', vehicleTypeId: spot?.vehicleType?.id ?? null });
    this.spotDialog.set({ editing: spot });
  }

  saveSpot(): void {
    const dialog = this.spotDialog();
    if (!dialog) return;
    if (this.spotForm.invalid) {
      this.spotForm.markAllAsTouched();
      return;
    }
    const v = this.spotForm.getRawValue();
    const body = { spotNumber: v.spotNumber.trim(), vehicleType: { id: Number(v.vehicleTypeId) } };
    const request = dialog.editing ? this.api.updateSpot(dialog.editing.id, body) : this.api.createSpot(body);
    request.subscribe({
      next: (saved) => {
        this.spotDialog.set(null);
        this.refresh(() => this.success(`Celda ${saved.spotNumber} ${dialog.editing ? 'actualizada' : 'creada'}.`));
      },
      error: (e: unknown) => this.dialogError.set(errorMessage(e))
    });
  }

  deleteSpot(spot: SpotDto): void {
    if (!confirm(`¿Eliminar la celda ${spot.spotNumber}?`)) return;
    this.api.deleteSpot(spot.id).subscribe({
      next: () => this.refresh(() => this.success(`Celda ${spot.spotNumber} eliminada.`)),
      error: (e: unknown) => this.fail(errorMessage(e))
    });
  }

  incidentCount(sessionId: number): number {
    return this.incidents().filter((incident) => incident.sessionId === sessionId).length;
  }

  /** Recarga estancias activas, historial y celdas desde el backend. */
  refresh(after?: () => void): void {
    this.loading.set(true);
    forkJoin({ active: this.api.getActiveSessions(), history: this.api.getHistory(), spots: this.api.getSpots() }).subscribe({
      next: ({ active, history, spots }) => {
        this.sessions.set(active.map((dto) => this.toSession(dto)));
        this.history.set(history.content.map((dto) => this.toSession(dto)));
        this.spots.set(spots.content);
        this.loading.set(false);
        after?.();
      },
      error: (error: unknown) => {
        this.loading.set(false);
        this.fail(errorMessage(error));
      }
    });
  }

  navigate(view: View): void {
    this.view.set(view);
    this.menuOpen.set(false);
    this.notice.set('');
    if (view !== 'entry') this.entryComplete.set(false);
    if (view !== 'exit' && view !== 'detail') this.exitSearched.set(false);
    if (view !== 'login') this.refresh();
    if (view === 'customers') this.loadCustomers();
  }

  searchVehicle(): void {
    const plate = this.entryForm.controls.plate.value.trim().toUpperCase();
    this.entryForm.controls.plate.setValue(plate);
    this.foundVehicle.set(null);
    this.vehicleLookupError.set('');
    this.entryForm.controls.spot.setValue('');
    if (this.entryForm.controls.plate.invalid) {
      this.entryForm.controls.plate.markAsTouched();
      return;
    }
    this.api.findVehicleByPlate(plate).subscribe({
      next: (vehicle) => {
        this.foundVehicle.set(vehicle);
        if (this.sessions().some((session) => session.vehicleId === vehicle.id)) {
          this.vehicleLookupError.set('Este vehículo ya se encuentra dentro del parqueadero.');
        }
      },
      error: (error: unknown) => this.vehicleLookupError.set(errorMessage(error))
    });
  }

  registerEntry(): void {
    const vehicle = this.foundVehicle();
    if (this.entryForm.invalid || !vehicle) {
      this.entryForm.markAllAsTouched();
      return;
    }
    const spotId = Number(this.entryForm.controls.spot.value);
    this.api.registerEntry(vehicle.id, spotId).subscribe({
      next: (dto) => {
        this.entryComplete.set(true);
        this.foundVehicle.set(null);
        this.entryForm.reset();
        this.refresh(() => this.success(`Entrada registrada: ${dto.vehicle.licensePlate} fue asignado a la celda ${dto.spot.spotNumber}.`));
      },
      // Rechazos del backend: vehículo duplicado, celda ocupada, tipo incompatible...
      error: (error: unknown) => {
        const message = errorMessage(error);
        this.fail(message);
        this.refresh(() => this.fail(message));
      }
    });
  }

  searchExit(): void {
    this.exitForm.markAllAsTouched();
    const plate = this.exitForm.controls.plate.value.trim().toUpperCase();
    if (this.exitForm.invalid) {
      this.selectedSession.set(null);
      this.exitSearched.set(false);
      return;
    }
    this.exitForm.controls.plate.setValue(plate);
    this.api.getActiveSessions().subscribe({
      next: (active) => {
        this.sessions.set(active.map((dto) => this.toSession(dto)));
        this.selectedSession.set(this.sessions().find((session) => normalizePlate(session.plate) === normalizePlate(plate)) ?? null);
        this.exitSearched.set(true);
      },
      error: (error: unknown) => this.fail(errorMessage(error))
    });
  }

  registerExit(): void {
    const session = this.selectedSession();
    if (!session) return;
    this.api.registerExit(session.id).subscribe({
      next: (dto) => {
        this.selectedSession.set(null);
        this.exitSearched.set(false);
        this.exitForm.reset();
        // La estancia pasa al historial (persistido en el backend) y su celda real queda libre.
        this.refresh(() => this.success(`Salida registrada: la celda ${dto.spot.spotNumber} quedó disponible.`));
      },
      error: (error: unknown) => {
        const message = errorMessage(error);
        this.selectedSession.set(null);
        this.refresh(() => this.fail(message));
      }
    });
  }

  addIncident(session: Session): void {
    this.incidentTarget.set(session);
    this.incidentForm.reset({ category: 'Observación', description: '' });
  }

  saveIncident(): void {
    const session = this.incidentTarget();
    if (!session || this.incidentForm.invalid) {
      this.incidentForm.markAllAsTouched();
      return;
    }
    const incident: Incident = {
      id: Date.now(),
      sessionId: session.id,
      plate: session.plate,
      spot: session.spot,
      category: this.incidentForm.controls.category.value,
      description: this.incidentForm.controls.description.value.trim(),
      createdAt: formatDate(new Date().toISOString())
    };
    this.incidents.update((items) => [incident, ...items]);
    this.persistIncidents();
    this.success(`Novedad registrada para ${session.plate}.`);
    this.incidentTarget.set(null);
  }

  showDetails(session: Session): void {
    this.selectedSession.set(session);
    this.navigate('detail');
  }

  showIncidentSession(sessionId: number): void {
    const session = [...this.sessions(), ...this.history()].find((item) => item.id === sessionId);
    if (session) this.showDetails(session);
  }

  private toSession(dto: SessionDto): Session {
    const vehicle = [dto.vehicle.brand, dto.vehicle.model].filter(Boolean).join(' ') || 'Vehículo registrado';
    return {
      id: dto.id,
      vehicleId: dto.vehicle.id,
      plate: dto.vehicle.licensePlate,
      vehicle,
      customer: dto.vehicle.customer?.name ?? 'Sin cliente',
      spot: dto.spot.spotNumber,
      enteredAt: formatDate(dto.entryTime),
      exitedAt: dto.exitTime ? formatDate(dto.exitTime) : null,
      operator: dto.entryOperator.name
    };
  }

  private success(message: string): void {
    this.noticeIsError.set(false);
    this.notice.set(message);
  }

  private fail(message: string): void {
    this.noticeIsError.set(true);
    this.notice.set(message);
  }

  // Las novedades aún no tienen endpoint en el backend: se conservan en el navegador (localStorage).
  private loadIncidents(): Incident[] {
    try {
      return JSON.parse(localStorage.getItem(INCIDENTS_KEY) ?? '[]') as Incident[];
    } catch {
      return [];
    }
  }

  private persistIncidents(): void {
    try {
      localStorage.setItem(INCIDENTS_KEY, JSON.stringify(this.incidents()));
    } catch {
      /* almacenamiento no disponible: se mantiene solo en memoria */
    }
  }
}
