import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HotelRoom, Reservation } from '../../models/models';
import { CommonModule } from '@angular/common';
import { HotelService } from '../../services/hotel.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
    styleUrls: ['./home.component.css'],

    imports: [CommonModule, FormsModule]

})
export class HomeComponent implements OnInit {
  availableRooms: HotelRoom[] = [];
  reservations: Reservation[]  = [];
  searchCriteria = {start: '', end:''};
  guestCountDate = '';
  guestCount: number | null = null;

  constructor(
    private authService: AuthService,
    private hotelService: HotelService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadReservations();
  }

  searchRooms(){
    if(this.searchCriteria.start && this.searchCriteria.end){
      this.hotelService.getRooms(this.searchCriteria.start, this.searchCriteria.end)
      .subscribe({
        next: (rooms) => {
          this.availableRooms = rooms;
        }
      })
    }
  }

  reserveRoom(room : HotelRoom){
    if(this.searchCriteria.start && this.searchCriteria.end){
      const reservation = {
        hotelRoomId: room.id,
        start: this.searchCriteria.start,
        end: this.searchCriteria.end
      };

      this.hotelService.reserveRoom(reservation).subscribe({
        next: () => {
          this.loadReservations();
          this.availableRooms = this.availableRooms.filter(r => r.id !== room.id) 
        }
      })
    }
  }


  loadReservations(){
    this.hotelService.getReservations().subscribe({
      next: (reservations) => {
        this.reservations = this.reservations;
      }
    })
  }


  getGuestCount(){
    if(this.guestCountDate){
      this.hotelService.getGuestCount(this.guestCountDate).subscribe({
        next: (count) => {
          this.guestCount = count;
        }
      })
    }
  }

  // onAuthorClick(author: Author) {
  //         console.log('Is authenticated:', this.authService.isAuthenticated()); // Check this

  //   this.router.navigate(['/author', author.id]);
  // }

  // onCreateDocument() {
  //   this.router.navigate(['/create-document']);
  // }

  
}
