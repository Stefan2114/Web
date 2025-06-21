import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface Ship {
  row: number;
  col: number;
}

interface Move {
  row: number;
  col: number;
  hit: boolean;
}

interface Game {
  id: number;
  player1Id: number;
  player2Id: number;
  currentPlayerId: number;
  status: string;
  winnerId: number;
  createdAt: string;
  updatedAt: string;
}

interface MoveResult {
  success: boolean;
  hit: boolean;
  gameWon: boolean;
  winnerId: number;
}

interface GameBoard {
  success: boolean;
  board: {
    playerShips: Ship[];
    playerMoves: Move[];
    opponentAttacks: Move[];
    gridSize: number;
    shipsPerPlayer: number;
  };
}

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private baseUrl = 'http://localhost:8080/game';

  constructor(private http: HttpClient) {}

  joinOrCreateGame(): Observable<{success: boolean, game: Game}> {
    return this.http.post<{success: boolean, game: Game}>(`${this.baseUrl}/join`, {}, { withCredentials: true });
  }

  placeShips(gameId: number, ships: Ship[]): Observable<{success: boolean, game: Game}> {
    return this.http.post<{success: boolean, game: Game}>(`${this.baseUrl}/${gameId}/ships`, ships, { withCredentials: true });
  }

  makeMove(gameId: number, row: number, col: number): Observable<MoveResult> {
    return this.http.post<MoveResult>(`${this.baseUrl}/${gameId}/move`, { row, col }, { withCredentials: true });
  }

  getGameStatus(gameId: number): Observable<{success: boolean, status: string, currentPlayer: number, isYourTurn: boolean}> {
    return this.http.get<{success: boolean, status: string, currentPlayer: number, isYourTurn: boolean}>(`${this.baseUrl}/${gameId}/status`, { withCredentials: true });
  }

  getGameBoard(gameId: number): Observable<GameBoard> {
    return this.http.get<GameBoard>(`${this.baseUrl}/${gameId}/board`, { withCredentials: true });
  }
}
