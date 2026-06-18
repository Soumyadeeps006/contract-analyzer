package com.example.contract.controller;

import com.example.contract.model.DocumentEntity;
import com.example.contract.service.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentEntity> uploadDocument(@RequestParam("file") MultipartFile file) {
        DocumentEntity saved = documentService.storeDocument(file);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Optional<DocumentEntity> maybeDoc = documentService.getDocumentMetadata(id);
        if (maybeDoc.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        DocumentEntity doc = maybeDoc.get();
        byte[] content = documentService.loadDocumentContent(doc.getStorageKey());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .body(content);
    }
}
