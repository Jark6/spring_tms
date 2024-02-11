package com.jark.TMS.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="TASKS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "task_id")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long task_id;
    @ManyToOne
    @JoinColumn(name="task_type_id")
    private TaskType task_type_id;
    @ManyToOne
    @JoinColumn(name="status_id")
    private Status status_id;
    @Column
    private String short_description;
    @Column
    private String full_description;
    @Column(nullable = true)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Long linked_task_id;
    @ManyToOne
    @JoinColumn(name = "linked_task_type_id")
    private LinkedTaskType linked_task_type_id;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project_id;
    @ManyToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "user_id")
    private Users executor_id;
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team_id;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private Users author_id;
    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = true)
    private Priority priority_id;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Comments> comments = new ArrayList<>();
    @Column
    private LocalDateTime timestamp_create;
    @Column
    private LocalDateTime timestamp_edit;

    public Tasks(TaskType task_type_id, Status status_id, String short_description, String full_description, Long linked_task_id, LinkedTaskType linked_task_type_id, @NotNull(message = "Не указана дата") Date deadline, Project project_id, Users executor_id, Users author_id, Priority priority_id, Long teamOrUserId, LocalDateTime timestampCreate) {
    }
    public Tasks(TaskType task_type_id, Status status_id, String short_description, String full_description,
                 Long linked_task_id, LinkedTaskType linked_task_type_id, Date deadline, Project project_id,
                 Users executor_id, Users author_id, Priority priority_id, LocalDateTime timestampCreate,
                 LocalDateTime timestampEdit) {
        this.task_type_id = task_type_id;
        this.status_id = status_id;
        this.short_description = short_description;
        this.full_description = full_description;
        this.linked_task_id = linked_task_id;
        this.linked_task_type_id = linked_task_type_id;
        this.deadline = deadline;
        this.project_id = project_id;
        this.executor_id = executor_id;
        //this.team_id=team_id;
        this.author_id = author_id;
        this.priority_id = priority_id;
        this.timestamp_create = timestampCreate;
        this.timestamp_edit = timestampEdit;
        this.comments = comments;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public TaskType getTask_type_id() {
        return task_type_id;
    }

    public void setTask_type_id(TaskType task_type_id) {
        this.task_type_id = task_type_id;
    }

    public Status getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Status status_id) {
        this.status_id = status_id;
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

    public void setLinked_task_id(Long linked_task_id) {
        this.linked_task_id = linked_task_id;
    }

    public LinkedTaskType getLinked_task_type_id() {
        return linked_task_type_id;
    }

    public void setLinked_task_type_id(LinkedTaskType linked_task_type_id) {
        this.linked_task_type_id = linked_task_type_id;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Project getProject_id() {
        return project_id;
    }

    public void setProject_id(Project project_id) {
        this.project_id = project_id;
    }

    public Users getExecutor_id() {
        return executor_id;
    }

    public void setExecutor_id(Users executor_id) {
        this.executor_id = executor_id;
    }

    public Users getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Users author_id) {
        this.author_id = author_id;
    }

    public Priority getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(Priority priority_id) {
        this.priority_id = priority_id;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp_create() {
        return timestamp_create;
    }

    public void setTimestamp_create(LocalDateTime timestamp_create) {
        this.timestamp_create = timestamp_create;
    }

    public LocalDateTime getTimestamp_edit() {
        return timestamp_edit;
    }

    public void setTimestamp_edit(LocalDateTime timestamp_edit) {
        this.timestamp_edit = timestamp_edit;
    }

    public Team getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Team team_id) {
        this.team_id = team_id;
    }
}
