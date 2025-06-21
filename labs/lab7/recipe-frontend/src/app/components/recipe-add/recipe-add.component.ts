import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeService } from '../../services/recipe.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-recipe-add',
  imports: [CommonModule, FormsModule],
  template: `
    <h1>Add Recipe</h1>
    <form (ngSubmit)="submit()">
      <input [(ngModel)]="name" name="name" placeholder="Name" required /><br />
      <input [(ngModel)]="author" name="author" placeholder="Author" required /><br />
      <input [(ngModel)]="type" name="type" placeholder="Type" required /><br />
      <textarea [(ngModel)]="content" name="content" placeholder="Content" required></textarea><br />
      <button type="submit" [disabled]="submitting">{{ submitting ? 'Adding...' : 'Add' }}</button>
      <button type="button" (click)="cancel()">Cancel</button>
    </form>
  `,
})
export class RecipeAddComponent {
  name = '';
  author = '';
  type = '';
  content = '';
  submitting = false;

  constructor(private service: RecipeService, private router: Router) {}

  submit() {
    if (!this.name || !this.author || !this.type || !this.content) {
      alert('Please fill in all fields');
      return;
    }
    
    this.submitting = true;
    this.service
      .addRecipe({ name: this.name, author: this.author, type: this.type, content: this.content })
      .subscribe({
        next: () => {
          alert('Recipe added successfully');
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('Error adding recipe:', err);
          alert('Error adding recipe');
          this.submitting = false;
        }
      });
  }
  
  cancel() {
    this.router.navigate(['/']);
  }
}