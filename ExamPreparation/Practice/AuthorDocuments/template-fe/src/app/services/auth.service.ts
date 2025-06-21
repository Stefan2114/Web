import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<string | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    const user = sessionStorage.getItem('currentUser');
    if (user) {
      this.currentUserSubject.next(user);
    }
  }

  login(credentials: LoginRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/login`, credentials, { 
      responseType: 'text',
      withCredentials: true 
    }).pipe(
      tap(() => {this.currentUserSubject.next(credentials.username);
                  sessionStorage.setItem('currentUser', credentials.username);}

    ));
  }

  logout(): Observable<string> {
    return this.http.post(`${this.apiUrl}/logout`, {}, { 
      responseType: 'text',
      withCredentials: true 
    }).pipe(
      tap(() => {this.currentUserSubject.next(null);
        sessionStorage.removeItem('currentUser');
      })
    );
  }

  isAuthenticated(): boolean {
    return this.currentUserSubject !== null;
  }
}
