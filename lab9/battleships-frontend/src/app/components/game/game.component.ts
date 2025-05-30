import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../services/game.service';
import { AuthService } from '../../services/auth.service';
import { interval, Subscription } from 'rxjs';

interface Ship {
  row: number;
  col: number;
}

interface Move {
  row: number;
  col: number;
  hit: boolean;
}

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {
  gameId: number | null = null;
  gameStatus = '';
  isMyTurn = false;
  gridSize = 8;
  shipsPerPlayer = 2;
  
  // Game states
  isPlacingShips = false;
  isGameStarted = false;
  gameWon = false;
  winnerId: number | null = null;
  
  // Ship placement
  selectedShips: Ship[] = [];
  
  // Game boards
  myShips: Ship[] = [];
  myMoves: Move[] = [];
  opponentAttacks: Move[] = [];
  
  // UI state
  loading = false;
  error = '';
  statusMessage = '';
  
  private pollingSubscription?: Subscription;

  constructor(
    private gameService: GameService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.joinGame();
  }

  ngOnDestroy() {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
  }

  joinGame() {
    this.loading = true;
    this.gameService.joinOrCreateGame().subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success) {
          this.gameId = response.game.id;
          this.updateGameState(response.game);
          this.startPolling();
        }
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to join game';
      }
    });
  }

  updateGameState(game: any) {
    this.gameStatus = game.status;
    this.isMyTurn = this.authService.getUserId() === game.currentPlayerId;
    this.winnerId = game.winnerId || null;
    
    switch (game.status) {
      case 'WAITING_FOR_PLAYERS':
        this.statusMessage = 'Waiting for another player to join...';
        break;
      case 'WAITING_FOR_SHIP_PLACEMENT':
        this.statusMessage = 'Place your ships on the board';
        this.isPlacingShips = true;
        break;
      case 'IN_PROGRESS':
        this.statusMessage = this.isMyTurn ? 'Your turn - Click on opponent board to attack' : 'Waiting for opponent move...';
        this.isGameStarted = true;
        this.loadGameBoard();
        break;
      case 'FINISHED':
        this.gameWon = true;
        this.statusMessage = this.winnerId === this.authService.getUserId() ? 'You won!' : 'You lost!';
        break;
    }
  }

  startPolling() {
    this.pollingSubscription = interval(2000).subscribe(() => {
      if (this.gameId) {
        this.gameService.getGameStatus(this.gameId).subscribe({
          next: (response) => {
            if (response.success) {
              const wasMyTurn = this.isMyTurn;
              this.isMyTurn = response.isYourTurn;
              
              if (response.status !== this.gameStatus) {
                this.gameStatus = response.status;
                this.updateGameState({
                  status: response.status,
                  currentPlayerId: response.currentPlayer,
                  winnerId: null
                });
              }
              
              if (this.isGameStarted && wasMyTurn !== this.isMyTurn) {
                this.loadGameBoard();
              }
            }
          }
        });
      }
    });
  }

  loadGameBoard() {
    if (this.gameId) {
      this.gameService.getGameBoard(this.gameId).subscribe({
        next: (response) => {
          if (response.success) {
            this.myShips = response.board.playerShips;
            this.myMoves = response.board.playerMoves;
            this.opponentAttacks = response.board.opponentAttacks;
          }
        }
      });
    }
  }

  onCellClick(row: number, col: number, isMyBoard: boolean) {
    if (isMyBoard && this.isPlacingShips) {
      this.placeShip(row, col);
    } else if (!isMyBoard && this.isGameStarted && this.isMyTurn) {
      this.makeMove(row, col);
    }
  }

  placeShip(row: number, col: number) {
    if (this.selectedShips.length >= this.shipsPerPlayer) {
      return;
    }
    
    // Check if position already occupied
    if (this.selectedShips.some(ship => ship.row === row && ship.col === col)) {
      return;
    }
    
    this.selectedShips.push({ row, col });
    
    if (this.selectedShips.length === this.shipsPerPlayer) {
      this.submitShipPlacement();
    }
  }

  submitShipPlacement() {
    if (this.gameId && this.selectedShips.length === this.shipsPerPlayer) {
      this.loading = true;
      this.gameService.placeShips(this.gameId, this.selectedShips).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.isPlacingShips = false;
            this.myShips = this.selectedShips;
            this.updateGameState(response.game);
          }
        },
        error: (error) => {
          this.loading = false;
          this.error = error.error?.message || 'Failed to place ships';
        }
      });
    }
  }

  makeMove(row: number, col: number) {
    if (this.gameId) {
      this.loading = true;
      this.gameService.makeMove(this.gameId, row, col).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            // Add the move to our moves list
            this.myMoves.push({ row, col, hit: response.hit });
            
            if (response.gameWon) {
              this.gameWon = true;
              this.statusMessage = 'You won!';
            } else {
              this.isMyTurn = false;
              this.statusMessage = 'Waiting for opponent move...';
            }
          }
        },
        error: (error) => {
          this.loading = false;
          this.error = error.error?.message || 'Failed to make move';
        }
      });
    }
  }

  getCellClass(row: number, col: number, isMyBoard: boolean): string {
    const classes = ['cell'];
    
    if (isMyBoard) {
      // My board
      if (this.isPlacingShips) {
        if (this.selectedShips.some(ship => ship.row === row && ship.col === col)) {
          classes.push('ship');
        }
        classes.push('clickable');
      } else {
        if (this.myShips.some(ship => ship.row === row && ship.col === col)) {
          classes.push('ship');
        }
        
        const attack = this.opponentAttacks.find(attack => attack.row === row && attack.col === col);
        if (attack) {
          classes.push(attack.hit ? 'hit' : 'miss');
        }
      }
    } else {
      // Opponent board
      const move = this.myMoves.find(move => move.row === row && move.col === col);
      if (move) {
        classes.push(move.hit ? 'hit' : 'miss');
      } else if (this.isGameStarted && this.isMyTurn) {
        classes.push('clickable');
      }
    }
    
    return classes.join(' ');
  }

  getGridArray(): number[] {
    return Array.from({ length: this.gridSize }, (_, i) => i);
  }
}