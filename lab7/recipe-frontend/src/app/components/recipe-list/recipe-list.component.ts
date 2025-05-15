import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Recipe, RecipeService } from '../../services/recipe.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-recipe-list',
  imports: [CommonModule, FormsModule],
  template: `
    <h1>Browse Recipes</h1>
    <label>Filter by type:</label>
    <input [(ngModel)]="filterType" />
    <button (click)="load()">Load</button>
    <p *ngIf="lastFilter">Last used filter: {{ lastFilter }}</p>
    <ul>
      <li *ngFor="let r of recipes">
        <strong>{{ r.name }}</strong> by {{ r.author }}
        <button (click)="edit(r.id!)">Edit</button>
        <button (click)="remove(r.id!)">Delete</button>
      </li>
    </ul>
    <button (click)="goToAdd()">Add New Recipe</button>
  `,
})
export class RecipeListComponent implements OnInit {
  recipes: Recipe[] = [];
  filterType = '';
  lastFilter = '';

  constructor(private service: RecipeService, private router: Router) {}
  
  ngOnInit() {
    // Load recipes on initialization with empty filter
    this.load();
  }

  load() {
    this.service.getRecipesByType(this.filterType).subscribe({
      next: (data) => {
        this.recipes = data;
        this.lastFilter = this.filterType;
      },
      error: (err) => {
        console.error('Error loading recipes:', err);
        alert('Error loading recipes. Check the console for details.');
      }
    });
  }

  edit(id: number) {
    this.router.navigate(['/edit', id]);
  }

  remove(id: number) {
    if (confirm('Are you sure you want to delete this recipe?')) {
      this.service.deleteRecipe(id).subscribe({
        next: () => this.load(),
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
}