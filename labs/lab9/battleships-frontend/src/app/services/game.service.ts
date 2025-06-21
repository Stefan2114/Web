import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface GameStatus {
  success: boolean;
  gameId?: number;
  player1?: string;
  player2?: string;
  currentPlayer?: string;
  gameStatus?: string;
  winner?: string;
  myGrid?: string;
  myAttacks?: string;
  message?: string;
}

export interface AttackResult {
  success: boolean;
  hit: boolean;
  gameOver: boolean;
  winner?: string;
}

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private apiUrl = 'http://localhost:8080/api/game';

  constructor(private http: HttpClient) {}

  joinGame(): Observable<any> {
    return this.http.get(`${this.apiUrl}/join`, { withCredentials: true });
  }

  getGameStatus(): Observable<GameStatus> {
    return this.http.get<GameStatus>(`${this.apiUrl}/status`, { withCredentials: true });
  }

  setupShips(grid: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/setup`, { grid }, { withCredentials: true });
  }

  makeAttack(row: number, col: number): Observable<AttackResult> {
    return this.http.post<AttackResult>(`${this.apiUrl}/attack`, 
      { row, col }, 
      { withCredentials: true }
    );
  }
}
