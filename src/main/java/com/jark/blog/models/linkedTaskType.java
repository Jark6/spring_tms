package com.jark.blog.models;

import jakarta.persistence.*;

@Entity
@Table(name="LINLED_TASK_TYPE")
public class linkedTaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long linked_task_type_id;
    @Column
    private String linked_task_type;

    public linkedTaskType() {
    }

    public linkedTaskType(String linked_task_type) {
        this.linked_task_type = linked_task_type;
    }

    public void setLinked_task_type_id(Long linkedTaskTypeId) {
        this.linked_task_type_id = linkedTaskTypeId;
    }

    public Long getLinked_task_type_id() {
        return linked_task_type_id;
    }

    public String getLinked_task_type() {
        return linked_task_type;
    }

    public void setLinked_task_type(String linked_task_type) {
        this.linked_task_type = linked_task_type;
    }
}
