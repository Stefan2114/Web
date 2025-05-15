import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe, RecipeService } from '../../services/recipe.service';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-recipe-edit',
  imports: [CommonModule, FormsModule],
  template: `
    <h1>Edit Recipe</h1>
    <div *ngIf="loading">Loading recipe data...</div>
    <form *ngIf="!loading" (ngSubmit)="submit()">
      <input [(ngModel)]="recipe.name" name="name" required /><br />
      <input [(ngModel)]="recipe.author" name="author" required /><br />
      <input [(ngModel)]="recipe.type" name="type" required /><br />
      <textarea [(ngModel)]="recipe.content" name="content" required></textarea><br />
      <button type="submit">Update</button>
      <button type="button" (click)="cancel()">Cancel</button>
    </form>
  `,
})
export class RecipeEditComponent implements OnInit {
  recipe: Recipe = { name: '', author: '', type: '', content: '' };
  loading = true;
  
  constructor(
    private route: ActivatedRoute,
    private service: RecipeService,
    private router: Router
  ) {}
  
  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(id)) {
      alert('Invalid recipe ID');
      this.router.navigate(['/']);
      return;
    }
    
    this.service.getRecipeById(id).subscribe({
      next: (data) => {
        this.recipe = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading recipe:', err);
        alert('Error loading recipe details');
        this.router.navigate(['/']);
      }
    });
  }
  
  submit() {
    this.service.updateRecipe(this.recipe).subscribe({
      next: () => {
        alert('Recipe updated successfully');
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Error updating recipe:', err);
        alert('Error updating recipe');
      }
    });
  }
  
  cancel() {
    this.router.navigate(['/']);
  }
}