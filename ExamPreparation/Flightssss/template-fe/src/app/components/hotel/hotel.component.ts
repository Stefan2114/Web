import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Hotel, Reservation } from '../../models/models';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-hotel',
  imports: [CommonModule],
  templateUrl: './hotel.component.html',
  styleUrl: './hotel.component.css'
})
export class HotelComponent implements OnInit{





  availableHotels: Hotel[] = [];

  constructor(
    private apiService: ApiService,
    private router: Router
    
  ) {}
  ngOnInit(): void {
    this.loadHotels();
  }

    loadHotels(){
      this.apiService.getAvailableHotels().subscribe({
        next: (hotels) => {
          this.availableHotels = hotels;
        }
      })
    }
  
    onClick(hotel: Hotel){
      const reservation: Reservation = {
        idReservedResource: hotel.id,
        type: 'hotel',
       person :sessionStorage.getItem("username") ?? ''
      }
      this.apiService.reserve(reservation).subscribe({
        next: () => {
          this.loadHotels();
        }
      })
    }
  
  
}

