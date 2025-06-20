import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import {Task, UpdatedTask } from '../../models/model';

@Component({
  selector: 'app-feedback-app',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  username: string = '';
  isUserLoggedIn:boolean = false;
  BACKEND_URL = 'http://localhost:8080/api';
  todoTaskList: Task[] = [];
  inProgressTaskList: Task[] = [];
  doneTaskList: Task[] = [];
  totalMoved: number = 0;



  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const storedUser = sessionStorage.getItem('currentUser');
    if (storedUser) {
      this.username = storedUser;
      //this.loadFeedback();
    }
  }

  get isLoggedIn(): boolean {
    return this.isUserLoggedIn;
  }

  login(): void {
    if (!this.username.trim()) {
      alert("Please enter your name.");
      return;
    }

    this.http.post<any>(`${this.BACKEND_URL}/login`, this.username).subscribe({
      next: (response) => {
          sessionStorage.setItem('username', this.username);
          this.isUserLoggedIn = true;
          this.loadTasks();
      },
      error: () => alert('Error logging in.')
    });
  }



  updateTask(task: Task): void {
  

    this.http.post<any>(`${this.BACKEND_URL}/tasks`, task).subscribe({
      next: (response) => {
          sessionStorage.setItem('username', this.username);
          this.isUserLoggedIn = true;
          this.loadTasks();
      },
      error: () => alert('Error logging in.')
    });
  }


  loadTasks(): void {
    this.http.get<Task[]>(`${this.BACKEND_URL}/tasks`).subscribe({
      next: (data) => {
        const taskList: Task[] = data;
        this.todoTaskList = taskList.filter(task => task.status === 'todo')
        this.inProgressTaskList = taskList.filter(task => task.status === 'in_progress')
        this.doneTaskList = taskList.filter(task => task.status === 'done')
      },
      error: () => alert('Error fetching feedback.')
    });
  }

  moveToInProgress(task: Task): void{



    console.log("Hot here he")
    const newTask:UpdatedTask = {id:task.id, newStatus: "in_progress",username: this.username};
        this.http.put<any>(`${this.BACKEND_URL}/tasks`, newTask).subscribe({
      next: (response) => {
        this.totalMoved = this.totalMoved + 1;
          this.loadTasks();

      },
      error: () => alert('Error logging in.')
    });
  }

    moveToDone(task: Task): void{
          console.log("Hot here he")

    const newTask:UpdatedTask = {id:task.id, newStatus: "done",username: this.username};
        this.http.put<any>(`${this.BACKEND_URL}/tasks`, newTask).subscribe({
      next: (response) => {
                this.totalMoved = this.totalMoved + 1;

          this.loadTasks();

      },
      error: () => alert('Error logging in.')
    });
  }

    moveToToDo(task: Task): void{
          console.log("Hot here he")

    const newTask:UpdatedTask = {id:task.id, newStatus: "todo",username: this.username};
        this.http.put<any>(`${this.BACKEND_URL}/tasks`, newTask).subscribe({
      next: (response) => {        this.totalMoved = this.totalMoved + 1;


          this.loadTasks();
      },
      error: () => alert('Error logging in.')
    });
  }
}

  // addFeedback(): void {
  //   this.http.post<AddFeedbackResponse>(`${this.BACKEND_URL}/addFeedback`, {
  //     customerName: this.username,
  //     message: this.message
  //   }).subscribe({
  //     next: (data) => {
  //       if (data.isNaughty) {
  //         this.naughtyCnt++;
  //         this.flaggedCount = this.naughtyCnt;
  //         this.highlightedMessage = data.highlightedMessage ?? "";

  //         if (this.naughtyCnt === 3) {
  //           alert("You have been restricted due to too many flagged messages.");
  //         }
  //       }
  //       this.loadFeedback();
  //     },
  //     error: () => alert('Failed to submit feedback.')
  //   });
  // }

  // cleanFeedback(): void {
  //   this.http.get(`${this.BACKEND_URL}/cleanMessage`, {
  //     params: { message: this.message },
  //     responseType: 'text'
  //   }).subscribe({
  //     next: cleanedMessage => {
  //       this.message = cleanedMessage;
  //       this.highlightedMessage = '';
  //     },
  //     error: () => alert('Failed to clean message.')
  //   });
  // }
