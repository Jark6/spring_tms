package com.jark.TMS.models;

import jakarta.persistence.*;

    @Entity
    @Table(name="TASK_TYPE")
    public class TaskType {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long task_type_id;
        @Column
        private String task_type;

        public TaskType(){
        }

        public TaskType(String task_type) {
            this.task_type = task_type;
        }

        public Long getTask_type_id() {
            return task_type_id;
        }

        public void setTask_type_id(Long task_type_id) {
            this.task_type_id = task_type_id;
        }

        public String getTask_type() {
            return task_type;
        }

        public void setTask_type(String task_type) {
            this.task_type = task_type;
        }
    }
