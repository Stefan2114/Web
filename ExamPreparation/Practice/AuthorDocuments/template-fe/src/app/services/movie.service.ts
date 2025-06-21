import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private apiUrl = 'http://localhost:8080/api/movies';

  constructor(private http: HttpClient) {}

  deleteMovie(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { 
      responseType: 'text',
      withCredentials: true 
    });
  }
}