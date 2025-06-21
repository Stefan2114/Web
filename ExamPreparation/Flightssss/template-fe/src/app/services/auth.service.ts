import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api';
  private authenticatedSubject = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) {}
  

  login(username: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, username, { 
      observe: 'response',
      withCredentials: true 
    }).pipe(
      tap((response) => {
        if(response.status === 204){
          this.authenticatedSubject.next(true);
                  }

        }  
    ));
  }


  isAuthenticated(): boolean {
    return this.authenticatedSubject.value;
  }
}
