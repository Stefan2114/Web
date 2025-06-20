import { Routes } from '@angular/router';
import { FeedbackComponent } from './components/feedback/feedback.component';

export const routes: Routes = [
  { path: '',  component: FeedbackComponent },
  { path: '**', redirectTo: '' }
];
