package com.jark.TMS.models;

import jakarta.persistence.*;

@Entity
@Table(name="STATUS")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long status_id;
    @Column
    private String status;
    public Status(){
    }
    public Status(String status) {
        this.status = status;
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long status_id) {
        this.status_id = status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
