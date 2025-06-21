import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
    styleUrls: ['./home.component.css'],

    imports: [CommonModule, FormsModule]

})
export class HomeComponent {


    constructor(
    private router: Router
  ) {}
    navigateFlights(){
      this.router.navigate(['/flights']);

  }

  navigateHotels(){
      this.router.navigate(['/hotels']);

  }

  
}
