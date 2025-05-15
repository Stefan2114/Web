// src/app/services/recipe.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface Recipe {
  id?: number;
  name: string;
  author: string;
  type: string;
  content: string;
}

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private apiUrl = 'http://localhost/recipes-app/api.php';

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    console.error('API Error:', error);
    let errorMessage = 'Something went wrong. Please try again later.';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client error: ${error.error.message}`;
    } else if (error.error && typeof error.error === 'object' && 'error' in error.error) {
      // Server returned an error object
      errorMessage = `Server error: ${error.error.error}`;
    } else if (error.status) {
      // Server returned an error status
      errorMessage = `Server returned status ${error.status}: ${error.statusText}`;
    }
    
    return throwError(() => new Error(errorMessage));
  }

  getRecipesByType(type: string): Observable<Recipe[]> {
    return this.http
      .get<Recipe[]>(`${this.apiUrl}?action=filter&type=${encodeURIComponent(type)}`)
      .pipe(
        tap(data => console.log('Received recipes:', data)),
        catchError(this.handleError)
      );
  }

  getRecipeById(id: number): Observable<Recipe> {
    return this.http
      .get<Recipe>(`${this.apiUrl}?action=get&id=${id}`)
      .pipe(catchError(this.handleError));
  }

  addRecipe(recipe: Recipe): Observable<any> {
    // Use URLSearchParams for x-www-form-urlencoded format
    const params = new URLSearchParams();
    
    // Add each recipe property to the params
    Object.entries(recipe).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        params.append(key, value.toString());
      }
    });
    
    // Add the action parameter
    params.append('action', 'add');
    
    // Make the POST request with the correct content type
    return this.http
      .post(this.apiUrl, params.toString(), {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      })
      .pipe(catchError(this.handleError));
  }

  updateRecipe(recipe: Recipe): Observable<any> {
    // Use URLSearchParams for x-www-form-urlencoded format
    const params = new URLSearchParams();
    
    // Add each recipe property to the params
    Object.entries(recipe).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        params.append(key, value.toString());
      }
    });
    
    // Add the action parameter
    params.append('action', 'update');
    
    // Make the POST request with the correct content type
    return this.http
      .post(this.apiUrl, params.toString(), {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      })
      .pipe(catchError(this.handleError));
  }

  deleteRecipe(id: number): Observable<any> {
    // Use URLSearchParams for x-www-form-urlencoded format
    const params = new URLSearchParams();
    
    // Add the id and action parameters
    params.append('id', id.toString());
    params.append('action', 'delete');
    
    // Make the POST request with the correct content type
    return this.http
      .post(this.apiUrl, params.toString(), {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      })
      .pipe(catchError(this.handleError));
  }
}