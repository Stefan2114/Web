import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { AddFeedbackResponse, Feedback } from '../../models/model';

@Component({
  selector: 'app-feedback-app',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {
  currentUser: string = '';
  email: string = '';
  message: string = '';
  isUserLoggedIn:boolean = false;
  naughtyCnt = 0;
  BACKEND_URL = 'http://localhost:8080/api';
  feedbackList: Feedback[] = [];
  flaggedCount = 0;
  highlightedMessage: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const storedUser = sessionStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUser = storedUser;
      this.loadFeedback();
    }
  }

  get isLoggedIn(): boolean {
    return this.isUserLoggedIn;
  }

  setUser(): void {
    if (!this.currentUser.trim() || !this.email.trim()) {
      alert("Please enter your name and email.");
      return;
    }

    const body = new HttpParams()
      .set('username', this.currentUser)
      .set('email', this.email);

    this.http.post<boolean>(`${this.BACKEND_URL}/authorize`, body.toString(), {
      headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' })
    }).subscribe({
      next: (isAuthorized) => {
        if (isAuthorized) {
          sessionStorage.setItem('currentUser', this.currentUser);
          this.isUserLoggedIn = true;
          this.loadFeedback();
        } else {
          alert('Authorization Failed');
        }
      },
      error: () => alert('Error logging in.')
    });
  }

  logout(): void {
    sessionStorage.removeItem('currentUser');
    this.isUserLoggedIn = false;
    this.currentUser = '';
    this.email = '';
    this.message = '';
    this.naughtyCnt = 0;
    this.flaggedCount = 0;
    this.feedbackList = [];
    this.highlightedMessage = '';
  }

  loadFeedback(): void {
    this.http.get<Feedback[]>(`${this.BACKEND_URL}/getFeedback`).subscribe({
      next: (data) => {
        this.feedbackList = data;
      },
      error: () => alert('Error fetching feedback.')
    });
  }

  addFeedback(): void {
    this.http.post<AddFeedbackResponse>(`${this.BACKEND_URL}/addFeedback`, {
      customerName: this.currentUser,
      message: this.message
    }).subscribe({
      next: (data) => {
        if (data.isNaughty) {
          this.naughtyCnt++;
          this.flaggedCount = this.naughtyCnt;
          this.highlightedMessage = data.highlightedMessage ?? "";

          if (this.naughtyCnt === 3) {
            alert("You have been restricted due to too many flagged messages.");
          }
        }
        this.loadFeedback();
      },
      error: () => alert('Failed to submit feedback.')
    });
  }

  cleanFeedback(): void {
    this.http.get(`${this.BACKEND_URL}/cleanMessage`, {
      params: { message: this.message },
      responseType: 'text'
    }).subscribe({
      next: cleanedMessage => {
        this.message = cleanedMessage;
        this.highlightedMessage = '';
      },
      error: () => alert('Failed to clean message.')
    });
  }
}
