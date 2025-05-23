// src/app/components/login/login.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <h1>Login to Recipe Manager</h1>
      <form (ngSubmit)="login()" class="login-form">
        <div class="form-group">
          <label for="username">Username:</label>
          <input 
            id="username"
            type="text" 
            [(ngModel)]="username" 
            name="username" 
            placeholder="Enter username" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="password">Password:</label>
          <input 
            id="password"
            type="password" 
            [(ngModel)]="password" 
            name="password" 
            placeholder="Enter password" 
            required 
          />
        </div>
        
        <button type="submit" [disabled]="submitting">
          {{ submitting ? 'Logging in...' : 'Login' }}
        </button>
        
        <div *ngIf="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </form>
    </div>
  `,
  styles: [`
    .login-container {
      max-width: 400px;
      margin: 100px auto;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 8px;
      background-color: #f9f9f9;
    }
    
    .login-form {
      display: flex;
      flex-direction: column;
    }
    
    .form-group {
      margin-bottom: 15px;
    }
    
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    
    input {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      box-sizing: border-box;
    }
    
    button {
      padding: 10px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    button:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }
    
    button:hover:not(:disabled) {
      background-color: #0056b3;
    }
    
    .error-message {
      color: red;
      margin-top: 10px;
      text-align: center;
    }
    
    h1 {
      text-align: center;
      margin-bottom: 20px;
    }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  submitting = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    if (!this.username || !this.password) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    this.submitting = true;
    this.errorMessage = '';

    this.authService.login({ username: this.username, password: this.password })
      .subscribe({
        next: () => {
          this.router.navigate(['/recipes']);
        },
        error: (err) => {
          console.error('Login error:', err);
          this.errorMessage = err.message || 'Login failed';
          this.submitting = false;
        }
      });
  }
}