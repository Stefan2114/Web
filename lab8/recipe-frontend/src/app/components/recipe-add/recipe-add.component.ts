// src/app/components/recipe-add/recipe-add.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeService } from '../../services/recipe.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-recipe-add',
  imports: [CommonModule, FormsModule],
  template: `
    <div class="add-container">
      <h1>Add New Recipe</h1>
      
      <form (ngSubmit)="submit()" class="add-form">
        <div class="form-group">
          <label for="name">Recipe Name:</label>
          <input 
            id="name"
            [(ngModel)]="name" 
            name="name" 
            placeholder="Enter recipe name" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="author">Author:</label>
          <input 
            id="author"
            [(ngModel)]="author" 
            name="author" 
            placeholder="Enter author name" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="type">Recipe Type:</label>
          <input 
            id="type"
            [(ngModel)]="type" 
            name="type" 
            placeholder="e.g., Dessert, Main Course, Appetizer" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="content">Recipe Instructions:</label>
          <textarea 
            id="content"
            [(ngModel)]="content" 
            name="content" 
            placeholder="Enter detailed recipe instructions" 
            rows="8"
            required
          ></textarea>
        </div>
        
        <div class="form-actions">
          <button type="submit" [disabled]="submitting">
            {{ submitting ? 'Adding...' : 'Add Recipe' }}
          </button>
          <button type="button" (click)="cancel()">Cancel</button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .add-container {
      max-width: 600px;
      margin: 20px auto;
      padding: 20px;
    }
    
    .add-form {
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
      background-color: #007bff;
      color: white;
    }
    
    button[type="submit"]:hover:not(:disabled) {
      background-color: #0056b3;
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
export class RecipeAddComponent implements OnInit {
  name = '';
  author = '';
  type = '';
  content = '';
  submitting = false;

  constructor(
    private recipeService: RecipeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    }
  }

  submit() {
    if (!this.name || !this.author || !this.type || !this.content) {
      alert('Please fill in all fields');
      return;
    }

    this.submitting = true;
    this.recipeService
      .addRecipe({ 
        name: this.name, 
        author: this.author, 
        type: this.type, 
        content: this.content 
      })
      .subscribe({
        next: () => {
          alert('Recipe added successfully');
          this.router.navigate(['/recipes']);
        },
        error: (err) => {
          console.error('Error adding recipe:', err);
          alert('Error adding recipe');
          this.submitting = false;
        }
      });
  }

  cancel() {
    if (confirm('Are you sure you want to cancel? Any entered data will be lost.')) {
      this.router.navigate(['/recipes']);
    }
  }
}