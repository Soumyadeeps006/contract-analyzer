package com.example.contract.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contract_analysis")
public class ContractAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private DocumentEntity document;

    @ElementCollection
    @CollectionTable(name = "extracted_clauses", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "clause")
    private List<String> extractedClauses;

    @ElementCollection
    @CollectionTable(name = "non_compliant_clauses", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "clause")
    private List<String> nonCompliantClauses;

    @ElementCollection
    @CollectionTable(name = "suggested_redlines", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "redline")
    private List<String> suggestedRedlines;

    // pgvector embedding column
    @Column(name = "embedding", columnDefinition = "vector")
    @Type(org.hibernate.type.VectorType.class)
    private float[] embedding;

    private LocalDateTime analyzedAt;

    // getters and setters omitted for brevity
}
