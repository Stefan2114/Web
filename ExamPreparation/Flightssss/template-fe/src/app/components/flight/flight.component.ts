import { Component, OnInit } from '@angular/core';
import { Flight, Reservation } from '../../models/models';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-flight',
  templateUrl: './flight.component.html',
  styleUrl: './flight.component.css',
  imports: [CommonModule]
  
})
export class FlightComponent implements OnInit{


  availableFlights: Flight[] = [];

  constructor(
    private apiService: ApiService,
    private router: Router
    
  ) {}
  ngOnInit(): void {
    this.loadFlights();
  }

  loadFlights(){
    this.apiService.getAvailableFlights().subscribe({
      next: (flights) => {
        this.availableFlights = flights;
      }
    })
  }

  onFlightClick(flight: Flight){
    const reservation: Reservation = {
      idReservedResource: flight.id,
      type: 'flight',
     person :sessionStorage.getItem("username") ?? ''
    }
    this.apiService.reserve(reservation).subscribe({
      next: () => {
        this.loadFlights();
      }
    })
  }

}
