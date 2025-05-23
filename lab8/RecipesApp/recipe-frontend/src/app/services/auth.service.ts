// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface User {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:5041/api/auth'; 
  private currentUserSubject = new BehaviorSubject<string | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    const user = sessionStorage.getItem('currentUser');
    if (user) {
      this.currentUserSubject.next(user);
    }
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Auth API Error:', error);
    let errorMessage = 'Something went wrong. Please try again later.';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client error: ${error.error.message}`;
    } else if (error.error && typeof error.error === 'object' && 'error' in error.error) {
      errorMessage = `Server error: ${error.error.error}`;
    } else if (error.status) {
      errorMessage = `Server returned status ${error.status}: ${error.statusText}`;
    }
    
    return throwError(() => new Error(errorMessage));
  }

  login(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user, { withCredentials: true })
      .pipe(
        tap((response: any) => {
          if (response.message === 'Logged in') {
            this.currentUserSubject.next(user.username);
            sessionStorage.setItem('currentUser', user.username);
          }
        }),
        catchError(this.handleError)
      );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true })
      .pipe(
        tap(() => {
          this.currentUserSubject.next(null);
          sessionStorage.removeItem('currentUser');
        }),
        catchError(this.handleError)
      );
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  getCurrentUser(): string | null {
    return this.currentUserSubject.value;
  }
}
