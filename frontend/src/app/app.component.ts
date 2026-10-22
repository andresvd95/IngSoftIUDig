import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

type View = 'login' | 'dashboard' | 'entry' | 'exit' | 'sessions' | 'spots' | 'incidents' | 'detail';
type SpotStatus = 'Disponible' | 'Ocupada' | 'Fuera de servicio';

interface Session {
  id: number;
  plate: string;
  vehicle: string;
  customer: string;
  spot: string;
  enteredAt: string;
  incidents: number;
}

interface ParkingSpot {
  code: string;
  type: string;
  status: SpotStatus;
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

@Component({
  selector: 'ac-root',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  readonly view = signal<View>('dashboard');
  readonly notice = signal('');
  readonly menuOpen = signal(false);
  readonly plateSearched = signal(false);
  readonly entryComplete = signal(false);
  readonly selectedSession = signal<Session | null>(null);
  readonly incidentTarget = signal<Session | null>(null);
  readonly incidents = signal<Incident[]>([
    { id: 1, sessionId: 2, plate: 'JKL 45F', spot: 'M-01', category: 'Observación', description: 'Espejo izquierdo con rayón al ingreso.', createdAt: '21 oct, 08:12' }
  ]);

  readonly sessions = signal<Session[]>([
    { id: 1, plate: 'ABC 123', vehicle: 'Chevrolet Onix', customer: 'Laura Gómez', spot: 'A-01', enteredAt: '21 oct, 07:42', incidents: 0 },
    { id: 2, plate: 'JKL 45F', vehicle: 'Yamaha FZ 2.0', customer: 'Carlos Pérez', spot: 'M-01', enteredAt: '21 oct, 08:10', incidents: 1 },
    { id: 3, plate: 'MNO 789', vehicle: 'Mazda 2', customer: 'Sofía Ramírez', spot: 'A-03', enteredAt: '21 oct, 08:25', incidents: 0 }
  ]);

  readonly spots = signal<ParkingSpot[]>([
    { code: 'A-01', type: 'Automóvil', status: 'Ocupada' },
    { code: 'A-02', type: 'Automóvil', status: 'Disponible' },
    { code: 'A-03', type: 'Automóvil', status: 'Ocupada' },
    { code: 'A-04', type: 'Automóvil', status: 'Disponible' },
    { code: 'M-01', type: 'Motocicleta', status: 'Ocupada' },
    { code: 'M-02', type: 'Motocicleta', status: 'Disponible' },
    { code: 'M-03', type: 'Motocicleta', status: 'Fuera de servicio' }
  ]);

  readonly availableSpots = computed(() => this.spots().filter((spot) => spot.status === 'Disponible'));
  readonly occupiedSpots = computed(() => this.spots().filter((spot) => spot.status === 'Ocupada'));
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

  navigate(view: View): void {
    this.view.set(view);
    this.menuOpen.set(false);
    this.notice.set('');
    if (view !== 'entry') this.entryComplete.set(false);
  }

  searchVehicle(): void {
    const plate = this.entryForm.controls.plate.value.trim().toUpperCase();
    this.entryForm.controls.plate.setValue(plate);
    this.plateSearched.set(this.entryForm.controls.plate.valid);
  }

  registerEntry(): void {
    if (this.entryForm.invalid || !this.plateSearched()) {
      this.entryForm.markAllAsTouched();
      return;
    }
    const plate = this.entryForm.controls.plate.value;
    const spot = this.entryForm.controls.spot.value;
    this.sessions.update((items) => [{ id: Date.now(), plate, vehicle: 'Vehículo registrado', customer: 'Cliente por confirmar', spot, enteredAt: 'Ahora', incidents: 0 }, ...items]);
    this.spots.update((items) => items.map((item) => item.code === spot ? { ...item, status: 'Ocupada' } : item));
    this.entryComplete.set(true);
    this.notice.set(`Entrada registrada: ${plate} fue asignado a la celda ${spot}.`);
  }

  searchExit(): void {
    this.exitForm.markAllAsTouched();
    const plate = this.exitForm.controls.plate.value.trim().toUpperCase();
    if (this.exitForm.invalid) {
      this.selectedSession.set(null);
      return;
    }
    this.exitForm.controls.plate.setValue(plate);
    this.selectedSession.set(this.sessions().find((session) => session.plate === plate) ?? null);
  }

  registerExit(): void {
    const session = this.selectedSession();
    if (!session) return;
    this.sessions.update((items) => items.filter((item) => item.id !== session.id));
    this.spots.update((items) => items.map((item) => item.code === session.spot ? { ...item, status: 'Disponible' } : item));
    this.notice.set(`Salida registrada: la celda ${session.spot} quedó disponible.`);
    this.selectedSession.set(null);
    this.exitForm.reset();
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
      createdAt: 'Ahora'
    };
    this.incidents.update((items) => [incident, ...items]);
    this.sessions.update((items) => items.map((item) => item.id === session.id ? { ...item, incidents: item.incidents + 1 } : item));
    this.notice.set(`Novedad registrada para ${session.plate}.`);
    this.incidentTarget.set(null);
  }

  showDetails(session: Session): void {
    this.selectedSession.set(session);
    this.navigate('detail');
  }

  showIncidentSession(sessionId: number): void {
    const session = this.sessions().find((item) => item.id === sessionId);
    if (session) this.showDetails(session);
  }
}
