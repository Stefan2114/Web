import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthorService } from '../../services/author.service';
import { MovieService } from '../../services/movie.service';
import { Document, Movie } from '../../models/models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-author-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './author-detail.component.html',
})
export class AuthorDetailComponent implements OnInit {
  authorId = 0;
  documents: Document[] = [];
  movies: Movie[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authorService: AuthorService,
    private movieService: MovieService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.authorId = +params['id'];
      this.authorService.getAuthorDocuments(this.authorId).subscribe(d => this.documents = d);
      this.authorService.getAuthorMovies(this.authorId).subscribe(m => this.movies = m);
    });
  }

  onDeleteMovie(movieId: number) {
    this.movieService.deleteMovie(movieId).subscribe(() => {
      this.movies = this.movies.filter(m => m.id !== movieId);
    });
  }

  onBack() {
    this.router.navigate(['/home']);
  }
}
