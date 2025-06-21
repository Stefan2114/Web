import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { GameService, GameStatus } from '../../services/game.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="game-container">
      <div class="game-header">
        <h2>Welcome, {{ currentUser?.username }}!</h2>
        <button class="btn btn-secondary" (click)="logout()">Logout</button>
      </div>

      <div class="game-status" *ngIf="gameStatus">
        <p><strong>Game Status:</strong> {{ gameStatus.gameStatus }}</p>
        <p *ngIf="gameStatus.player1"><strong>Player 1:</strong> {{ gameStatus.player1 }}</p>
        <p *ngIf="gameStatus.player2"><strong>Player 2:</strong> {{ gameStatus.player2 }}</p>
        <p *ngIf="gameStatus.currentPlayer"><strong>Current Turn:</strong> {{ gameStatus.currentPlayer }}</p>
        <p *ngIf="gameStatus.winner" class="winner"><strong>Winner:</strong> {{ gameStatus.winner }}</p>
      </div>

      <div class="game-actions" *ngIf="!gameJoined">
        <button class="btn btn-primary" (click)="joinGame()" [disabled]="isLoading">
          {{ isLoading ? 'Joining...' : 'Join Game' }}
        </button>
      </div>

      <div class="game-setup" *ngIf="gameJoined && !shipsPlaced && gameStatus?.gameStatus === 'ACTIVE'">
        <h3>Place Your Ships</h3>
        <p>Click on 2 cells to place your ships:</p>
        <div class="grid setup-grid">
          <div 
            *ngFor="let cell of myGrid; let i = index"
            class="cell"
            [class.ship]="cell === 1"
            (click)="placeShip(i)"
          >
          </div>
        </div>
        <button 
          class="btn btn-primary" 
          (click)="confirmShipPlacement()" 
          [disabled]="getShipCount() !== 2"
        >
          Confirm Ship Placement ({{ getShipCount() }}/2)
        </button>
      </div>

      <div class="game-board" *ngIf="shipsPlaced && gameStatus?.gameStatus === 'ACTIVE'">
        <div class="boards-container">
          <div class="board-section">
            <h3>Your Grid</h3>
            <div class="grid">
              <div 
                *ngFor="let cell of myGrid; let i = index"
                class="cell"
                [class.ship]="cell === 1"
                [class.hit]="getEnemyAttackAt(i) === 2"
                [class.miss]="getEnemyAttackAt(i) === 1"
              >
              </div>
            </div>
          </div>

          <div class="board-section">
            <h3>Enemy Grid</h3>
            <div class="grid">
              <div 
                *ngFor="let cell of enemyGrid; let i = index"
                class="cell attack-cell"
                [class.hit]="myAttacks[i] === 2"
                [class.miss]="myAttacks[i] === 1"
                [class.disabled]="!isMyTurn() || myAttacks[i] > 0"
                (click)="makeAttack(i)"
              >
              </div>
            </div>
          </div>
        </div>

        <div class="turn-indicator" *ngIf="!gameStatus?.winner">
          <p [class.my-turn]="isMyTurn()" [class.enemy-turn]="!isMyTurn()">
            {{ isMyTurn() ? 'Your Turn - Click on enemy grid to attack!' : 'Waiting for opponent...' }}
          </p>
        </div>
      </div>

      <div class="waiting-message" *ngIf="gameStatus?.gameStatus === 'WAITING'">
        <h3>Waiting for another player to join...</h3>
        <p>Share this game with a friend!</p>
      </div>

      <div class="error-message" *ngIf="errorMessage">
        {{ errorMessage }}
      </div>

      <div class="success-message" *ngIf="successMessage">
        {{ successMessage }}
      </div>
    </div>
  `,
  styles: [`
    .game-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem;
      color: white;
    }

    .game-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 2rem;
      background: rgba(255, 255, 255, 0.1);
      padding: 1rem;
      border-radius: 8px;
    }

    .game-status {
      background: rgba(255, 255, 255, 0.1);
      padding: 1rem;
      border-radius: 8px;
      margin-bottom: 2rem;
    }

    .game-status p {
      margin: 0.5rem 0;
    }

    .winner {
      color: #ffd700;
      font-size: 1.2rem;
    }

    .game-actions {
      text-align: center;
      margin: 2rem 0;
    }

    .game-setup {
      text-align: center;
      margin: 2rem 0;
    }

    .game-setup h3 {
      margin-bottom: 1rem;
    }

    .boards-container {
      display: flex;
      gap: 2rem;
      justify-content: center;
      flex-wrap: wrap;
    }

    .board-section {
      text-align: center;
    }

    .board-section h3 {
      margin-bottom: 1rem;
    }

    .grid {
      display: grid;
      grid-template-columns: repeat(5, 40px);
      grid-template-rows: repeat(5, 40px);
      gap: 2px;
      margin: 0 auto;
      background: rgba(0, 0, 0, 0.2);
      padding: 10px;
      border-radius: 8px;
    }

    .cell {
      width: 40px;
      height: 40px;
      background: rgba(255, 255, 255, 0.8);
      border: 1px solid #ccc;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
    }

    .cell.ship {
      background: #4CAF50;
      color: white;
    }

    .cell.hit {
      background: #f44336;
      color: white;
    }

    .cell.miss {
      background: #2196F3;
      color: white;
    }

    .cell.disabled {
      cursor: not-allowed;
      opacity: 0.6;
    }

    .attack-cell:hover:not(.disabled) {
      background: rgba(255, 255, 255, 0.9);
    }

    .setup-grid .cell:hover {
      background: rgba(76, 175, 80, 0.7);
    }

    .turn-indicator {
      text-align: center;
      margin: 2rem 0;
      font-size: 1.2rem;
    }

    .my-turn {
      color: #4CAF50;
      font-weight: bold;
    }

    .enemy-turn {
      color: #FF9800;
    }

    .waiting-message {
      text-align: center;
      margin: 2rem 0;
      padding: 2rem;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 8px;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      font-size: 1rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .btn-primary {
      background-color: #667eea;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background-color: #5a6fd8;
    }

    .btn-secondary {
      background-color: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background-color: #5a6268;
    }

    .btn:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }

    .error-message {
      color: #ff6b6b;
      background: rgba(255, 107, 107, 0.1);
      padding: 1rem;
      border-radius: 4px;
      margin: 1rem 0;
      text-align: center;
    }

    .success-message {
      color: #51cf66;
      background: rgba(81, 207, 102, 0.1);
      padding: 1rem;
      border-radius: 4px;
      margin: 1rem 0;
      text-align: center;
    }

    @media (max-width: 768px) {
      .boards-container {
        flex-direction: column;
        gap: 1rem;
      }
      
      .game-header {
        flex-direction: column;
        gap: 1rem;
      }
    }
  `]
})
export class GameComponent implements OnInit, OnDestroy {
  currentUser: any;

  gameStatus: GameStatus | null = null;
  gameJoined = false;
  shipsPlaced = false;
  myGrid: number[] = new Array(25).fill(0);
  enemyGrid: number[] = new Array(25).fill(0);
  myAttacks: number[] = new Array(25).fill(0);
  enemyAttacks: number[] = new Array(25).fill(0);
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  private statusSubscription?: Subscription;

  constructor(
    private authService: AuthService,
    private gameService: GameService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
    this.joinGame();
  }


  ngOnDestroy(): void {
    if (this.statusSubscription) {
      this.statusSubscription.unsubscribe();
    }
  }

  joinGame(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.gameService.joinGame().subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.success) {
          this.gameJoined = true;
          this.successMessage = response.message;
          this.startStatusPolling();
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to join game';
      }
    });
  }

  startStatusPolling(): void {
    this.statusSubscription = interval(2000).subscribe(() => {
      this.getGameStatus();
    });
    this.getGameStatus(); // Initial call
  }

  getGameStatus(): void {
    this.gameService.getGameStatus().subscribe({
      next: (status) => {
        if (status.success) {
          this.gameStatus = status;
          if (status.myGrid) {
            this.parseGridData(status.myGrid);
            this.shipsPlaced = true;
          }
          if (status.myAttacks) {
            this.myAttacks = status.myAttacks.split(',').map(Number);
          }
        }
      },
      error: (error) => {
        console.error('Error getting game status:', error);
      }
    });
  }

  parseGridData(gridData: string): void {
    this.myGrid = gridData.split(',').map(Number);
  }

  placeShip(index: number): void {
    if (this.getShipCount() < 2) {
      this.myGrid[index] = this.myGrid[index] === 1 ? 0 : 1;
    }
  }

  getShipCount(): number {
    return this.myGrid.filter(cell => cell === 1).length;
  }

  confirmShipPlacement(): void {
    if (this.getShipCount() === 2) {
      const gridData = this.myGrid.join(',');
      this.gameService.setupShips(gridData).subscribe({
        next: (response) => {
          if (response.success) {
            this.shipsPlaced = true;
            this.successMessage = 'Ships placed successfully!';
          } else {
            this.errorMessage = response.message;
          }
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Failed to place ships';
        }
      });
    }
  }

  makeAttack(index: number): void {
    if (!this.isMyTurn() || this.myAttacks[index] > 0) {
      return;
    }

    const row = Math.floor(index / 5);
    const col = index % 5;

    this.gameService.makeAttack(row, col).subscribe({
      next: (result) => {
        if (result.success) {
          this.myAttacks[index] = result.hit ? 2 : 1;
          
          if (result.gameOver && result.winner) {
            this.successMessage = result.winner === this.currentUser?.username 
              ? 'Congratulations! You won!' 
              : 'Game Over! You lost.';
          }
          
          this.getGameStatus(); // Refresh game status
        } else {
          this.errorMessage = 'Invalid attack';
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Attack failed';
      }
    });
  }

  getEnemyAttackAt(index: number): number {
    return this.enemyAttacks[index] || 0;
  }

  isMyTurn(): boolean {
    return this.gameStatus?.currentPlayer === this.currentUser?.username;
  }

  logout(): void {
    if (confirm('Are you sure you want to logout? This will end your current game.')) {
      this.authService.logout().subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: () => {
          this.router.navigate(['/login']);
        }
      });
    }
  }
}