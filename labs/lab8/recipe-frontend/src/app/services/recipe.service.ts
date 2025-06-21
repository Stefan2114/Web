
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
  private apiUrl = 'http://localhost:5041/api/recipes'; 

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    console.error('Recipe API Error:', error);
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

  getAllRecipes(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.apiUrl, { withCredentials: true })
      .pipe(
        tap(data => console.log('Received all recipes:', data)),
        catchError(this.handleError)
      );
  }

  getRecipesByType(type: string): Observable<Recipe[]> {
    const url = type ? `${this.apiUrl}/filter?type=${encodeURIComponent(type)}` : this.apiUrl;
    return this.http.get<Recipe[]>(url, { withCredentials: true })
      .pipe(
        tap(data => console.log('Received filtered recipes:', data)),
        catchError(this.handleError)
      );
  }

  getRecipeById(id: number): Observable<Recipe> {
    return this.http.get<Recipe>(`${this.apiUrl}/${id}`, { withCredentials: true })
      .pipe(catchError(this.handleError));
  }

  addRecipe(recipe: Recipe): Observable<any> {
    return this.http.post(`${this.apiUrl}`, recipe, { withCredentials: true })
      .pipe(catchError(this.handleError));
  }

  updateRecipe(recipe: Recipe): Observable<any> {
    return this.http.put(`${this.apiUrl}`, recipe, { withCredentials: true })
      .pipe(catchError(this.handleError));
  }

  deleteRecipe(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { withCredentials: true })
      .pipe(catchError(this.handleError));
  }
}