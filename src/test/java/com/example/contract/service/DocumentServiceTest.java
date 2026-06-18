package com.example.contract.service;

import com.example.contract.model.DocumentEntity;
import com.example.contract.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadDocument_savesMetadataAndReturnsEntity() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        DocumentEntity saved = new DocumentEntity();
        saved.setId(1L);
        saved.setFilename("test.pdf");
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(saved);
        DocumentEntity result = documentService.storeDocument(file);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test.pdf", result.getFilename());
    }
}
