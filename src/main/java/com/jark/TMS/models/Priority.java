package com.jark.TMS.models;

import jakarta.persistence.*;

@Entity
@Table(name="PRIORITY")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long priority_id;
    @Column
    private String priority;

    public Priority(){
    }

    public Priority(String priority) {
        this.priority = priority;
    }

    public Long getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(Long priority_id) {
        this.priority_id = priority_id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
