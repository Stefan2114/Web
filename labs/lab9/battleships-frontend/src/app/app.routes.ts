import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { GameComponent } from './components/game/game.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'game', component: GameComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];