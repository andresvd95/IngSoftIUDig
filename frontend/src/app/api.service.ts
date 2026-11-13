import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

/** Operador autenticado. El login es de demostración; coincide con el operador sembrado en AC1.4. */
export const CURRENT_OPERATOR_ID = 1;

export interface VehicleTypeDto { id: number; name: string; }
export interface CustomerFull {
  id: number;
  identificationType: string;
  identificationNumber: string;
  name: string;
  email?: string | null;
  phone?: string | null;
}
export interface CustomerPayload {
  identificationType: string;
  identificationNumber: string;
  name: string;
  email?: string | null;
  phone?: string | null;
}
export interface VehiclePayload {
  licensePlate: string;
  brand: string;
  model: number;
  color: string;
  customerId: number;
  vehicleTypeId: number;
}
export interface SpotPayload { spotNumber: string; vehicleType: { id: number }; }
export interface CustomerDto { id: number; identificationNumber: string; name: string; }
export interface VehicleDto {
  id: number;
  licensePlate: string;
  brand: string | null;
  model: number | null;
  color: string | null;
  vehicleType: VehicleTypeDto | null;
  customer: CustomerDto | null;
}
export interface SpotDto { id: number; spotNumber: string; available: boolean; vehicleType: VehicleTypeDto | null; }
export interface OperatorDto { id: number; name: string; }
export interface SessionDto {
  id: number;
  entryTime: string;
  exitTime: string | null;
  vehicle: Omit<VehicleDto, 'vehicleType'>;
  spot: { id: number; spotNumber: string };
  entryOperator: OperatorDto;
  exitOperator: OperatorDto | null;
}
export interface Page<T> { content: T[]; totalElements: number; }

/** Cliente de la API Spring Boot. En Docker/nginx y en `ng serve` se accede por el prefijo /api. */
@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api';

  findVehicleByPlate(licensePlate: string): Observable<VehicleDto> {
    return this.http.get<VehicleDto>(`${this.base}/vehicle`, { params: new HttpParams().set('licensePlate', licensePlate) });
  }

  getSpots(): Observable<Page<SpotDto>> {
    return this.http.get<Page<SpotDto>>(`${this.base}/parking-spot`, { params: new HttpParams().set('size', 200) });
  }

  getVehicleTypes(): Observable<VehicleTypeDto[]> {
    return this.http.get<VehicleTypeDto[]>(`${this.base}/vehicle-type`);
  }

  getCustomers(search: string): Observable<Page<CustomerFull>> {
    let params = new HttpParams().set('size', 100);
    if (search) params = params.set('search', search);
    return this.http.get<Page<CustomerFull>>(`${this.base}/customer`, { params });
  }
  createCustomer(body: CustomerPayload): Observable<CustomerFull> {
    return this.http.post<CustomerFull>(`${this.base}/customer`, body);
  }
  updateCustomer(id: number, body: CustomerPayload): Observable<CustomerFull> {
    return this.http.put<CustomerFull>(`${this.base}/customer/${id}`, body);
  }
  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/customer/${id}`);
  }

  getVehiclesByCustomer(customerId: number): Observable<VehicleDto[]> {
    return this.http.get<VehicleDto[]>(`${this.base}/vehicle`, { params: new HttpParams().set('customerId', customerId) });
  }
  createVehicle(body: VehiclePayload): Observable<VehicleDto> {
    return this.http.post<VehicleDto>(`${this.base}/vehicle`, body);
  }

  createSpot(body: SpotPayload): Observable<SpotDto> {
    return this.http.post<SpotDto>(`${this.base}/parking-spot`, body);
  }
  updateSpot(id: number, body: SpotPayload): Observable<SpotDto> {
    return this.http.put<SpotDto>(`${this.base}/parking-spot/${id}`, body);
  }
  deleteSpot(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/parking-spot/${id}`);
  }

  getActiveSessions(): Observable<SessionDto[]> {
    return this.http.get<SessionDto[]>(`${this.base}/parking-session/active`);
  }

  getHistory(): Observable<Page<SessionDto>> {
    return this.http.get<Page<SessionDto>>(`${this.base}/parking-session/history`, { params: new HttpParams().set('size', 100) });
  }

  registerEntry(vehicleId: number, spotId: number): Observable<SessionDto> {
    return this.http.post<SessionDto>(`${this.base}/parking-session/vehicle-entry`,
      { vehicleId, spotId, entryOperatorId: CURRENT_OPERATOR_ID });
  }

  registerExit(sessionId: number): Observable<SessionDto> {
    return this.http.post<SessionDto>(`${this.base}/parking-session/vehicle-exit`,
      { id: sessionId, exitOperatorId: CURRENT_OPERATOR_ID });
  }
}

/** Extrae el mensaje legible del ErrorResponseApi del backend (rechazos 404/409/400). */
export function errorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    if (error.status === 0) return 'No se pudo conectar con el servidor. Verifica que el backend esté en ejecución.';
    const message = (error.error as { message?: string } | null)?.message;
    if (message) return message;
    return `Error ${error.status}: no fue posible completar la operación.`;
  }
  return 'Ocurrió un error inesperado.';
}
