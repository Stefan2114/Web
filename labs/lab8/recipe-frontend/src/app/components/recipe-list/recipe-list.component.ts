// src/app/components/recipe-list/recipe-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Recipe, RecipeService } from '../../services/recipe.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-recipe-list',
  imports: [CommonModule, FormsModule],
  template: `
    <div class="recipe-list-container">
      <div class="header">
        <h1>Browse Recipes</h1>
        <div class="user-info">
          Welcome, {{ currentUser }}!
          <button (click)="logout()" class="logout-btn">Logout</button>
        </div>
      </div>
      
      <div class="filter-section">
        <label for="filterType">Filter by type:</label>
        <input 
          id="filterType"
          [(ngModel)]="filterType" 
          placeholder="Enter recipe type" 
        />
        <button (click)="load()">Load</button>
        <button (click)="clearFilter()">Clear Filter</button>
      </div>
      
      <p *ngIf="lastFilter" class="last-filter">
        Last used filter: "{{ lastFilter }}"
      </p>
      
      <div *ngIf="loading" class="loading">Loading recipes...</div>
      
      <ul *ngIf="!loading" class="recipe-list">
        <li *ngFor="let r of recipes" class="recipe-item">
          <div class="recipe-info">
            <strong>{{ r.name }}</strong> by {{ r.author }}
            <span class="recipe-type">({{ r.type }})</span>
          </div>
          <div class="recipe-actions">
            <button (click)="edit(r.id!)" class="edit-btn">Edit</button>
            <button (click)="remove(r.id!)" class="delete-btn">Delete</button>
          </div>
        </li>
      </ul>
      
      <div *ngIf="!loading && recipes.length === 0" class="no-recipes">
        No recipes found. {{ filterType ? 'Try a different filter or' : '' }} Add your first recipe!
      </div>
      
      <button (click)="goToAdd()" class="add-btn">Add New Recipe</button>
    </div>
  `,
  styles: [`
    .recipe-list-container {
      max-width: 800px;
      margin: 0 auto;
      padding: 20px;
    }
    
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
    }
    
    .logout-btn {
      padding: 5px 10px;
      background-color: #dc3545;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .logout-btn:hover {
      background-color: #c82333;
    }
    
    .filter-section {
      display: flex;
      gap: 10px;
      align-items: center;
      margin-bottom: 20px;
      padding: 15px;
      background-color: #f8f9fa;
      border-radius: 8px;
    }
    
    .filter-section input {
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      flex: 1;
    }
    
    .filter-section button {
      padding: 8px 15px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .filter-section button:hover {
      background-color: #0056b3;
    }
    
    .last-filter {
      font-style: italic;
      color: #666;
      margin-bottom: 15px;
    }
    
    .loading {
      text-align: center;
      padding: 20px;
      font-style: italic;
    }
    
    .recipe-list {
      list-style: none;
      padding: 0;
    }
    
    .recipe-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 15px;
      margin-bottom: 10px;
      border: 1px solid #ddd;
      border-radius: 8px;
      background-color: #fff;
    }
    
    .recipe-info {
      flex: 1;
    }
    
    .recipe-type {
      color: #666;
      font-size: 0.9em;
    }
    
    .recipe-actions {
      display: flex;
      gap: 10px;
    }
    
    .edit-btn {
      padding: 5px 10px;
      background-color: #28a745;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .edit-btn:hover {
      background-color: #218838;
    }
    
    .delete-btn {
      padding: 5px 10px;
      background-color: #dc3545;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .delete-btn:hover {
      background-color: #c82333;
    }
    
    .no-recipes {
      text-align: center;
      padding: 40px;
      color: #666;
      font-style: italic;
    }
    
    .add-btn {
      display: block;
      margin: 20px auto 0;
      padding: 12px 24px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }
    
    .add-btn:hover {
      background-color: #0056b3;
    }
  `]
})
export class RecipeListComponent implements OnInit {
  recipes: Recipe[] = [];
  filterType = '';
  lastFilter = '';
  loading = false;
  currentUser: string | null = null;

  constructor(
    private recipeService: RecipeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }
    this.load();
  }

  load() {
    this.loading = true;
    this.recipeService.getRecipesByType(this.filterType).subscribe({
      next: (data) => {
        this.recipes = data;
        this.lastFilter = this.filterType;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading recipes:', err);
        alert('Error loading recipes. Check the console for details.');
        this.loading = false;
      }
    });
  }

  clearFilter() {
    this.filterType = '';
    this.load();
  }

  edit(id: number) {
    this.router.navigate(['/edit', id]);
  }

  remove(id: number) {
    if (confirm('Are you sure you want to delete this recipe?')) {
      this.recipeService.deleteRecipe(id).subscribe({
        next: () => {
          alert('Recipe deleted successfully');
          this.load();
        },
        error: (err) => {
          console.error('Error deleting recipe:', err);
          alert('Error deleting recipe. Check the console for details.');
        }
      });
    }
  }

  goToAdd() {
    this.router.navigate(['/add']);
  }

  logout() {
    if (confirm('Are you sure you want to logout?')) {
      this.authService.logout().subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Logout error:', err);
          // Navigate to login anyway
          this.router.navigate(['/login']);
        }
      });
    }
  }
}