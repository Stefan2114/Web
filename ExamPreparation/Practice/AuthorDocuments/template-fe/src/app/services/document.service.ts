import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Document, DocumentDTO } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = 'http://localhost:8080/api/documents';

  constructor(private http: HttpClient) {}

  addDocument(document: DocumentDTO): Observable<Document> {
    return this.http.post<Document>(this.apiUrl, document, { withCredentials: true });
  }

  getLargestDocument(): Observable<Document> {
    return this.http.get<Document>(`${this.apiUrl}/largest`, { withCredentials: true });
  }
}