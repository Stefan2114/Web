import { Routes } from '@angular/router';
import { RecipeListComponent } from './components/recipe-list/recipe-list.component';
import { RecipeAddComponent } from './components/recipe-add/recipe-add.component';
import { RecipeEditComponent } from './components/recipe-edit/recipe-edit.component';

export const routes: Routes = [
  { path: '', component: RecipeListComponent },
  { path: 'add', component: RecipeAddComponent },
  { path: 'edit/:id', component: RecipeEditComponent },
];