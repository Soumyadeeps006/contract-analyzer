package com.example.contract;

import com.example.contract.model.DocumentEntity;
import com.example.contract.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void uploadDocument_returnsOk_andPersists() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "sample.pdf", "application/pdf", "dummy".getBytes());
        mockMvc.perform(multipart("/api/documents")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
        // Verify persistence
        assert(documentRepository.findAll().size() > 0);
    }
}
