import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

/** Operador autenticado. El login es de demostración; coincide con el operador sembrado en AC1.4. */
export const CURRENT_OPERATOR_ID = 1;

export interface VehicleTypeDto { id: number; name: string; }
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
