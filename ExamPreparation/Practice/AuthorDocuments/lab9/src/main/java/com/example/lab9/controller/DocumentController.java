package com.example.lab9.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab9.DTO.DocumentDTO;
import com.example.lab9.model.Document;
import com.example.lab9.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody DocumentDTO document) {

        System.out.println("Trying to add");
        try {
            Document savedDocument = this.documentService.addDocument(document);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/largest")
    public ResponseEntity<Document> getLargestNrOfAuthorsDocument() {
        try {
            return ResponseEntity.ok(this.documentService.largestNrOfAuthorsDocument());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
