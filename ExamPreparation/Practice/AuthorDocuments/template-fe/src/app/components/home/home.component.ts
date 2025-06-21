import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AuthorService } from '../../services/author.service';
import { DocumentService } from '../../services/document.service';
import { Author, Document } from '../../models/models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
    styleUrls: ['./home.component.css'],

    imports: [CommonModule]

})
export class HomeComponent implements OnInit {
  authors: Author[] = [];
  largestDocument: Document | null = null;

  constructor(
    private authService: AuthService,
    private authorService: AuthorService,
    private documentService: DocumentService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authorService.getAllAuthors().subscribe(authors => {
      this.authors = authors || [];
    });

    this.documentService.getLargestDocument().subscribe(doc => {
      this.largestDocument = doc || null;
    });
  }

  onAuthorClick(author: Author) {
          console.log('Is authenticated:', this.authService.isAuthenticated()); // Check this

    this.router.navigate(['/author', author.id]);
  }

  onCreateDocument() {
    this.router.navigate(['/create-document']);
  }

  onLogout() {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
}
