import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RecipeListComponent } from './components/recipe-list/recipe-list.component';
import { RecipeEditComponent } from './components/recipe-edit/recipe-edit.component';
import { RecipeAddComponent } from './components/recipe-add/recipe-add.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'recipes', component: RecipeListComponent, canActivate: [AuthGuard] },
  { path: 'add', component: RecipeAddComponent, canActivate: [AuthGuard] },
  { path: 'edit/:id', component: RecipeEditComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/login' }
];