package com.example.lab9.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lab9.DTO.DocumentDTO;
import com.example.lab9.model.Author;
import com.example.lab9.model.Document;
import com.example.lab9.model.DocumentAuthor;
import com.example.lab9.repo.AuthorRepo;
import com.example.lab9.repo.DocumentAuthorRepo;
import com.example.lab9.repo.DocumentRepo;

@Service
@Transactional
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final AuthorRepo authorRepo;
    private final DocumentAuthorRepo documentAuthorRepo;

    public DocumentService(DocumentRepo documentRepo, AuthorRepo authorRepo, DocumentAuthorRepo documentAuthorRepo) {
        this.documentRepo = documentRepo;
        this.authorRepo = authorRepo;
        this.documentAuthorRepo = documentAuthorRepo;

    }

    public Document addDocument(DocumentDTO documentDTO) {

        Document document = new Document();
        document.setName(documentDTO.name());
        document.setContent(documentDTO.content());
        document = this.documentRepo.save(document);

        for (String authorName : documentDTO.authors()) {
            Author author = this.authorRepo.findByName(authorName)
                    .orElseThrow(() -> new RuntimeException("Author with name: " + authorName + " not found"));
            DocumentAuthor documentAuthor = new DocumentAuthor();
            documentAuthor.setAuthor(author);
            documentAuthor.setDocument(document);
            this.documentAuthorRepo.save(documentAuthor);
            document.getAuthors().add(documentAuthor);
        }

        System.out.println("Document saved successfully");

        return document;

    }

    public List<Document> getAuthorDocuments(Integer authorId) {
        return this.documentAuthorRepo.findAllByAuthorId(authorId).stream()
                .map(documentAuthor -> documentAuthor.getDocument()).toList();
    }

    public Document largestNrOfAuthorsDocument() {
        return this.documentRepo.findAll().stream().sorted(
                (document1, document2) -> Integer.compare(document2.getAuthors().size(), document1.getAuthors().size()))
                .findFirst().orElseThrow(() -> new RuntimeException("No document found"));
    }
}
