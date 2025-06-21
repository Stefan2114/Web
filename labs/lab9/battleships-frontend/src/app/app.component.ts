import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  template: `
    <div class="app-container">
      <header class="app-header">
        <h1>Battleship Game</h1>
      </header>
      <main>
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .app-header {
      background: rgba(0, 0, 0, 0.2);
      padding: 1rem;
      text-align: center;
      color: white;
      margin-bottom: 2rem;
    }
    .app-header h1 {
      margin: 0;
      font-size: 2rem;
    }
  `]
})
export class AppComponent {
  title = 'battleship-frontend';
}