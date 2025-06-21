import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { AuthorDetailComponent } from './components/author-detail/author-detail.component';
import { CreateDocumentComponent } from './components/create-document/create-document.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'author/:id', component: AuthorDetailComponent, canActivate: [AuthGuard] },
  { path: 'create-document', component: CreateDocumentComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/login' }
];
