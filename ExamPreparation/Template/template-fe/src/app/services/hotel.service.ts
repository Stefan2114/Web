import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HotelRoom, Reservation, ReserveDTO } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  private apiUrl = '/api'

  constructor(private http: HttpClient) {
   }


   getRooms(start: string, end: string): Observable<HotelRoom[]> {
    const params = new HttpParams()
    .set('start', start)
    .set('end', end)

    return this.http.get<HotelRoom[]>(`${this.apiUrl}/rooms`, {params});
   }


   reserveRoom(reservation: ReserveDTO) : Observable<any> {
    return this.http.post(`${this.apiUrl}/reserve`, reservation, {
      observe: 'response'
    }); 
   }


   getReservations() : Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/reservations`)
   }


   getGuestCount(date: string): Observable<number> {
    const params = new HttpParams().set('date', date);
    return this.http.get<number>(`${this.apiUrl}/guest-count`, {params})
   }
}
