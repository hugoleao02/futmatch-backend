package br.com.futmatch.infrastructure.adapters.out.persistences.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


     @Column(name = "created_at", nullable = false, updatable = false)
     protected LocalDateTime createdAt;

     @Column(name = "updated_at", nullable = false)
     protected LocalDateTime updatedAt;


     @PrePersist
     protected void onCreate() {
         createdAt = LocalDateTime.now();
         updatedAt = LocalDateTime.now();
     }

     @PreUpdate
     protected void onUpdate() {
         updatedAt = LocalDateTime.now();
     }
}