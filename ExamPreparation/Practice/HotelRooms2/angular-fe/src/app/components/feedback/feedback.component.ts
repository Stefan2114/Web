import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Reservation, Flight, HotelRoom } from '../../models/model';

@Component({
  selector: 'app-feedback-app',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {
  currentUser: string = '';
  currentCity: string = '';
  currentDate?: Date;
  isUserLoggedIn:boolean = false;
  BACKEND_URL = 'http://localhost:8080/api';
  hotelList: HotelRoom[] = [];
  flightList: Flight[] = [];
  reservationList: Reservation[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const storedUser = sessionStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUser = storedUser;
    }
  }

  get isLoggedIn(): boolean {
    return this.isUserLoggedIn;
  }

  setUser(): void {
    if (!this.currentUser.trim() || !this.currentCity.trim() || !this.currentDate) {
      alert("Please enter all info.");
      return;
    }

          sessionStorage.setItem('currentUser', this.currentUser);
          this.isUserLoggedIn = true;
  }

  logout(): void {
    sessionStorage.removeItem('currentUser');
    this.isUserLoggedIn = false;
    this.currentUser = '';
    this.currentCity = '';
    this.currentDate = undefined;
    this.flightList = [];
    this.hotelList = [];
    this.addReservations();
    this.reservationList = [];

  }

  loadFlights(): void{

     if (!this.currentCity || !this.currentDate) {
    alert('Please provide both city and date.');
    return;
  }

  this.hotelList = [];

  const params = new HttpParams()
    .set('city', this.currentCity)
    .set('date', this.currentDate.toISOString().split('T')[0]);
    this.http.get<Flight[]>(`${this.BACKEND_URL}/flights`, { params }).subscribe({
    next: (data) => {
      this.flightList = data;
    },
    error: () => alert('Error fetching flights.')
  });
  }

loadHotels(): void {
  if (!this.currentCity || !this.currentDate) {
    alert('Please provide both city and date.');
    return;
  }

  this.flightList = [];

  const params = new HttpParams()
    .set('city', this.currentCity)
    .set('date', this.currentDate.toISOString().split('T')[0]);

  this.http.get<HotelRoom[]>(`${this.BACKEND_URL}/hotels`, { params }).subscribe({
    next: (data) => {
      this.hotelList = data.map(hotel => {
        const reservationCount = this.reservationList.filter(
          r => r.type === 'hotel' && r.typeId === hotel.id
        ).length;

        // Add availableRooms as a derived property
        return {
          ...hotel,
          availableRooms: hotel.nrRooms - reservationCount
        };
      });
    },
    error: () => alert('Error fetching hotels.')
  });
}
  clickHotel(hotel: HotelRoom): void{
    
const reservation: Reservation = {person: this.currentUser, typeId: hotel.id, type: "hotel"}
    this.reservationList.push(reservation);
    this.loadHotels();
    
    
  }

    clickFlights(flight: Flight): void{
    
const reservation: Reservation = {person: this.currentUser, typeId: flight.id, type: "flight"}
    this.reservationList.push(reservation);
    this.loadFlights();
    
    
  }

  

  addReservations(): void {
    this.http.post<any>(`${this.BACKEND_URL}/addFeedback`, {
      username: this.currentUser
    }).subscribe({
      next: (data) => {
        if (data.isNaughty) {
          this.naughtyCnt++;
          this.flaggedCount = this.naughtyCnt;
          this.highlightedMessage = data.highlightedMessage ?? "";

          if (this.naughtyCnt === 3) {
            alert("You have been restricted due to too many flagged messages.");
          }
        }
        this.loadFeedback();
      },
      error: () => alert('Failed to submit feedback.')
    });
  }
}
