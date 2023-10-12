package com.jark.TMS.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="TASKS")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long task_id;
    @Column
    private Long task_type_id;
    @Column
    private Long status_id;
    @Column
    private String short_description;
    @Column
    private String full_description;
    @Column
    private Long linked_task_id;
    @Column
    private Long linked_task_type_id;
    @Column
    private Date deadline;
    @Column
    private Long project_id;
    @Column
    private Long executor_id;
    @Column
    private Long author_id;
    @Column
    private Long priority_id;

    public Tasks() {
    }
    public Tasks(Long taskTypeId, Long status_id, String short_description, String full_description, Long linked_task_id, Long linked_task_type_id, Date deadline, Long project_id, Long executor_id, Long author_id, Long priority_id) {
        this.task_type_id = taskTypeId;
        this.status_id = status_id;
        this.short_description = short_description;
        this.full_description = full_description;
        this.linked_task_id = linked_task_id;
        this.linked_task_type_id = linked_task_type_id;
        this.deadline = deadline;
        this.project_id = project_id;
        this.executor_id = executor_id;
        this.author_id = author_id;
        this.priority_id = priority_id;
    }

    public Long getTaskId() {
        return task_id;
    }

    public void setTaskId(Long taskId) {
        this.task_id = taskId;
    }

    public Long getTaskTypeId() {
        return task_type_id;
    }

    public void setTaskTypeId(Long taskTypeId) {
        this.task_type_id = taskTypeId;
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long statusId) {
        this.status_id = statusId;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getFull_description() {
        return full_description;
    }

    public void setFull_description(String full_description) {
        this.full_description = full_description;
    }

    public Long getLinked_task_id() {
        return linked_task_id;
    }

    public void setLinked_task_id(Long linkedTaskId) {
        this.linked_task_id = linkedTaskId;
    }

    public Long getLinked_task_type_id() {
        return linked_task_type_id;
    }

    public void setLinked_task_type_id(Long linkedTaskTypeId) {
        this.linked_task_type_id = linkedTaskTypeId;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long projectId) {
        this.project_id = projectId;
    }

    public Long getExecutor_id() {
        return executor_id;
    }

    public void setExecutor_id(Long executorId) {
        this.executor_id = executorId;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long authorId) {
        this.author_id = authorId;
    }

    public Long getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(Long priorityId) {
        this.priority_id = priorityId;
    }
}
