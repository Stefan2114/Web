import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { FlightComponent } from './components/flight/flight.component';
import { HotelComponent } from './components/hotel/hotel.component';
import { MainLayoutComponent } from './layouts/main-layoput/main-layoput.component';
export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // ✅ Add this to define the login page
  { path: 'login', component: LoginComponent },

  // ✅ All routes that should display the navbar go here
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'home', component: HomeComponent },
      { path: 'flights', component: FlightComponent },
      { path: 'hotels', component: HotelComponent },
    ],
  },

  // ✅ Redirect unknown routes to a safe page (NOT /login)
  { path: '**', redirectTo: '/home' }
];