import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Flight, Hotel, Reservation } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {
   }


  //  getRooms(start: string, end: string): Observable<HotelRoom[]> {
  //   const params = new HttpParams()
  //   .set('start', start)
  //   .set('end', end)

  //   return this.http.get<Flight[]>(`${this.apiUrl}/flights`, {params});
  //  }


   reserve(reservation: Reservation) : Observable<any> {
        console.log("I got here");

    return this.http.post(`${this.apiUrl}/reservations`, reservation, {
      observe: 'response'
    }); 
   }


   getAvailableFlights() : Observable<Flight[]> {
    console.log("I got here");
    const date = sessionStorage.getItem("date") ?? '';
    const city = sessionStorage.getItem("city") ?? '';
    console.log(city)
    let params = new HttpParams();
    params = params.set('date', date);
    params = params.set('city', city);
    return this.http.get<Flight[]>(`${this.apiUrl}/flights-available`, { params });
   }


      getAvailableHotels() : Observable<Hotel[]> {
    console.log("I got here");
    const date = sessionStorage.getItem("date") ?? '';
    const city = sessionStorage.getItem("city") ?? '';
    console.log(city)
    let params = new HttpParams();
    params = params.set('date', date);
    params = params.set('city', city);
    return this.http.get<Hotel[]>(`${this.apiUrl}/hotels-available`, { params });
   }


  //  getGuestCount(date: string): Observable<number> {
  //   const params = new HttpParams().set('date', date);
  //   return this.http.get<number>(`${this.apiUrl}/guest-count`, {params})
  //  }
}
