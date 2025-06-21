import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-container">
      <div class="login-form-wrapper">
        <h2>Login to Battleship</h2>
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="login-form">
          <div class="form-group">
            <label for="username">Username:</label>
            <input 
              type="text" 
              id="username"
              formControlName="username" 
              class="form-control"
              [class.error]="loginForm.get('username')?.invalid && loginForm.get('username')?.touched"
            >
            <div class="error-message" *ngIf="loginForm.get('username')?.invalid && loginForm.get('username')?.touched">
              <span *ngIf="loginForm.get('username')?.errors?.['required']">Username is required</span>
              <span *ngIf="loginForm.get('username')?.errors?.['minlength']">Username must be at least 3 characters</span>
            </div>
          </div>

          <div class="form-group">
            <label for="password">Password:</label>
            <input 
              type="password" 
              id="password"
              formControlName="password" 
              class="form-control"
              [class.error]="loginForm.get('password')?.invalid && loginForm.get('password')?.touched"
            >
            <div class="error-message" *ngIf="loginForm.get('password')?.invalid && loginForm.get('password')?.touched">
              <span *ngIf="loginForm.get('password')?.errors?.['required']">Password is required</span>
              <span *ngIf="loginForm.get('password')?.errors?.['minlength']">Password must be at least 4 characters</span>
            </div>
          </div>

          <button 
            type="submit" 
            class="btn btn-primary"
            [disabled]="loginForm.invalid || isLoading"
          >
            {{ isLoading ? 'Logging in...' : 'Login' }}
          </button>

          <div class="error-message" *ngIf="errorMessage">
            {{ errorMessage }}
          </div>

          <div class="success-message" *ngIf="successMessage">
            {{ successMessage }}
          </div>
        </form>
        
        <p class="info-text">
          Enter any username and password. If the account doesn't exist, it will be created automatically.
        </p>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 70vh;
      padding: 2rem;
    }

    .login-form-wrapper {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 400px;
    }

    .login-form-wrapper h2 {
      text-align: center;
      margin-bottom: 1.5rem;
      color: #333;
    }

    .form-group {
      margin-bottom: 1rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: bold;
      color: #555;
    }

    .form-control {
      width: 100%;
      padding: 0.75rem;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 1rem;
      box-sizing: border-box;
    }

    .form-control:focus {
      outline: none;
      border-color: #667eea;
      box-shadow: 0 0 5px rgba(102, 126, 234, 0.3);
    }

    .form-control.error {
      border-color: #dc3545;
    }

    .btn {
      width: 100%;
      padding: 0.75rem;
      font-size: 1rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .btn-primary {
      background-color: #667eea;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background-color: #5a6fd8;
    }

    .btn:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }

    .error-message {
      color: #dc3545;
      font-size: 0.875rem;
      margin-top: 0.25rem;
    }

    .success-message {
      color: #28a745;
      font-size: 0.875rem;
      margin-top: 0.25rem;
    }

    .info-text {
      text-align: center;
      margin-top: 1rem;
      font-size: 0.875rem;
      color: #666;
    }
  `]
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });

    // Redirect if already authenticated
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/game']);
    }
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const { username, password } = this.loginForm.value;

      this.authService.login(username, password).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success) {
            this.successMessage = response.message;
            setTimeout(() => {
              this.router.navigate(['/game']);
            }, 1000);
          } else {
            this.errorMessage = response.message;
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.error?.message || 'Login failed. Please try again.';
        }
      });
    }
  }
}
