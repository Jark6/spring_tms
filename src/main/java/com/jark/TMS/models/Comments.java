package com.jark.TMS.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Tasks task;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private LocalDateTime timestamp;

    // Геттеры, сеттеры и другие методы

    public Comments() {
    }

    public Comments(Long id, String content, Tasks task, Users user, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.task = task;
        this.user = user;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Tasks getTask() {
        return task;
    }

    public void setTask(Tasks task) {
        this.task = task;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
