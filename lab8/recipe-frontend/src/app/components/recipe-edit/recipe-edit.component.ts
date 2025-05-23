// src/app/components/recipe-edit/recipe-edit.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe, RecipeService } from '../../services/recipe.service';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-recipe-edit',
  imports: [CommonModule, FormsModule],
  template: `
    <div class="edit-container">
      <h1>Edit Recipe</h1>
      
      <div *ngIf="loading" class="loading">Loading recipe data...</div>
      
      <form *ngIf="!loading" (ngSubmit)="submit()" class="edit-form">
        <div class="form-group">
          <label for="name">Recipe Name:</label>
          <input 
            id="name"
            [(ngModel)]="recipe.name" 
            name="name" 
            placeholder="Enter recipe name"
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="author">Author:</label>
          <input 
            id="author"
            [(ngModel)]="recipe.author" 
            name="author" 
            placeholder="Enter author name"
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="type">Recipe Type:</label>
          <input 
            id="type"
            [(ngModel)]="recipe.type" 
            name="type" 
            placeholder="e.g., Dessert, Main Course, Appetizer"
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="content">Recipe Instructions:</label>
          <textarea 
            id="content"
            [(ngModel)]="recipe.content" 
            name="content" 
            placeholder="Enter detailed recipe instructions"
            rows="8"
            required
          ></textarea>
        </div>
        
        <div class="form-actions">
          <button type="submit" [disabled]="submitting">
            {{ submitting ? 'Updating...' : 'Update Recipe' }}
          </button>
          <button type="button" (click)="cancel()">Cancel</button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .edit-container {
      max-width: 600px;
      margin: 20px auto;
      padding: 20px;
    }
    
    .loading {
      text-align: center;
      padding: 40px;
      font-style: italic;
    }
    
    .edit-form {
      display: flex;
      flex-direction: column;
    }
    
    .form-group {
      margin-bottom: 20px;
    }
    
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    
    input, textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
      box-sizing: border-box;
      font-family: inherit;
    }
    
    textarea {
      resize: vertical;
      min-height: 120px;
    }
    
    .form-actions {
      display: flex;
      gap: 10px;
      justify-content: flex-end;
    }
    
    button {
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }
    
    button[type="submit"] {
      background-color: #28a745;
      color: white;
    }
    
    button[type="submit"]:hover:not(:disabled) {
      background-color: #218838;
    }
    
    button[type="submit"]:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }
    
    button[type="button"] {
      background-color: #6c757d;
      color: white;
    }
    
    button[type="button"]:hover {
      background-color: #5a6268;
    }
    
    h1 {
      text-align: center;
      margin-bottom: 30px;
    }
  `]
})
export class RecipeEditComponent implements OnInit {
  recipe: Recipe = { name: '', author: '', type: '', content: '' };
  loading = true;
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(id)) {
      alert('Invalid recipe ID');
      this.router.navigate(['/recipes']);
      return;
    }

    this.recipeService.getRecipeById(id).subscribe({
      next: (data) => {
        this.recipe = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading recipe:', err);
        alert('Error loading recipe details');
        this.router.navigate(['/recipes']);
      }
    });
  }

  submit() {
    if (!this.recipe.name || !this.recipe.author || !this.recipe.type || !this.recipe.content) {
      alert('Please fill in all fields');
      return;
    }

    this.submitting = true;
    this.recipeService.updateRecipe(this.recipe).subscribe({
      next: () => {
        alert('Recipe updated successfully');
        this.router.navigate(['/recipes']);
      },
      error: (err) => {
        console.error('Error updating recipe:', err);
        alert('Error updating recipe');
        this.submitting = false;
      }
    });
  }

  cancel() {
    if (confirm('Are you sure you want to cancel? Any unsaved changes will be lost.')) {
      this.router.navigate(['/recipes']);
    }
  }
}