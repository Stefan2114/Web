import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Author, Document, Movie } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {
  private apiUrl = 'http://localhost:8080/api/authors';

  constructor(private http: HttpClient) {}

  getAllAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>(this.apiUrl, { withCredentials: true });
  }

  getAuthorDocuments(id: number): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/${id}/documents`, { withCredentials: true });
  }

  getAuthorMovies(id: number): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/${id}/movies`, { withCredentials: true });
  }
}