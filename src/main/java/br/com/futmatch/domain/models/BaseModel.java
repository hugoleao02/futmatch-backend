package br.com.futmatch.domain.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BaseModel() {
    }

    public BaseModel(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void initializeTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
} 