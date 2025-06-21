import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DocumentService } from '../../services/document.service';
import { DocumentDTO } from '../../models/models';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-document',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create-document.component.html',

})
export class CreateDocumentComponent {
  document: DocumentDTO = {
    name: '',
    content: '',
    authors: []
  };
  authorInput = '';

  constructor(
    private documentService: DocumentService,
    private router: Router
  ) {}

  addAuthor() {
    if (this.authorInput.trim()) {
      this.document.authors.push(this.authorInput.trim());
      this.authorInput = '';
    }
  }

  removeAuthor(index: number) {
    this.document.authors.splice(index, 1);
  }

  onSubmit() {
    this.documentService.addDocument(this.document).subscribe(() => {
      this.router.navigate(['/home']);
    });
  }

  onCancel() {
    this.router.navigate(['/home']);
  }
}
