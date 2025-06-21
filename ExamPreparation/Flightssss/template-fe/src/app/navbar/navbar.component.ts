// src/app/components/navbar/navbar.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true
})
export class NavbarComponent {
  constructor(private router: Router) {}

  navigateFlights() {
    this.router.navigate(['/flights']);
  }

  navigateHotels() {
    this.router.navigate(['/hotels']);
  }
}
