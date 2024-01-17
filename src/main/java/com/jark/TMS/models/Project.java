package com.jark.TMS.models;

import jakarta.persistence.*;

@Entity
@Table(name="PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long project_id;
    @Column
    private String project_name;
    @Column
    private String project_description;

    public Project() {
    }

    public Project(String project_name, String project_description) {
        this.project_name = project_name;
        this.project_description = project_description;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }
}
