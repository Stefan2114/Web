import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  registerForm: FormGroup;
  isLoginMode = true;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });

    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  switchMode() {
    this.isLoginMode = !this.isLoginMode;
    this.error = '';
  }

  onSubmit() {

    console.log('Submit called, isLoginMode:', this.isLoginMode);
  console.log('Register form valid:', this.registerForm.valid);
  console.log('Register form errors:', this.registerForm.errors);
  console.log('Username errors:', this.registerForm.get('username')?.errors);
  console.log('Password errors:', this.registerForm.get('password')?.errors);
  console.log('Form values:', this.registerForm.value);
    if (this.isLoginMode) {
      this.login();
    } else {
      this.register();
    }
  }

  login() {
    if (this.loginForm.valid) {
      this.loading = true;
      this.error = '';
      const { username, password } = this.loginForm.value;
      
      this.authService.login(username, password).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.router.navigate(['/game']);
          } else {
            this.error = response.message;
          }
        },
        error: (error) => {
          this.loading = false;
          this.error = error.error?.message || 'Login failed';
        }
      });
    }
  }

  register() {
    if (this.registerForm.valid) {
      this.loading = true;
      this.error = '';
      const { username, password } = this.registerForm.value;
      
      this.authService.register(username, password).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.router.navigate(['/game']);
          } else {
            this.error = response.message;
          }
        },
        error: (error) => {
          this.loading = false;
          this.error = error.error?.message || 'Registration failed';
        }
      });
    }
  }
}
