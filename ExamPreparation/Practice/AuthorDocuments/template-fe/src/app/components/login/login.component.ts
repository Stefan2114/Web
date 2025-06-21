import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/models';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule], 
  templateUrl: './login.component.html',
})
export class LoginComponent {
  credentials: LoginRequest = { username: '', password: '' };

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.credentials).subscribe(() => {
      console.log("I got here")
      this.router.navigate(['/home']);
    });
  }
}
