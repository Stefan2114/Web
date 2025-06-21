import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule], 
  templateUrl: './login.component.html',
})
export class LoginComponent {
    username = '';
    date = '';
    city = '';



  constructor(private authService: AuthService, private router: Router) {}

  beginReservation() {

    sessionStorage.setItem('username', this.username);
      sessionStorage.setItem('date', this.date);
      sessionStorage.setItem('city', this.city);
      this.router.navigate(['/home']);
  }
}
