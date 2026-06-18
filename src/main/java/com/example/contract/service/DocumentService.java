package com.example.contract.service;

import com.example.contract.model.DocumentEntity;
import com.example.contract.repository.DocumentRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.ObjectWriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final MinioClient minioClient;
    private final String bucketName = "contracts"; // ensure bucket exists via init script

    public DocumentService(DocumentRepository documentRepository, MinioClient minioClient) {
        this.documentRepository = documentRepository;
        this.minioClient = minioClient;
    }

    /**
     * Stores the uploaded file in MinIO and persists the metadata in PostgreSQL.
     */
    public DocumentEntity storeDocument(MultipartFile file) {
        try {
            // Generate a unique storage key
            String storageKey = UUID.randomUUID().toString() + "/" + file.getOriginalFilename();
            // Upload to MinIO
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storageKey)
                    .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            ObjectWriteResponse resp = minioClient.putObject(args);
            // Persist metadata
            DocumentEntity entity = new DocumentEntity();
            entity.setFilename(file.getOriginalFilename());
            entity.setContentType(file.getContentType());
            entity.setStorageKey(storageKey);
            return documentRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store document", e);
        }
    }

    public Optional<DocumentEntity> getDocumentMetadata(Long id) {
        return documentRepository.findById(id);
    }

    public byte[] loadDocumentContent(String storageKey) {
        try (var stream = minioClient.getObject(io.minio.GetObjectArgs.builder()
                .bucket(bucketName)
                .object(storageKey)
                .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load document content", e);
        }
    }
}
