package com.jark.TMS.controllers;

import com.jark.TMS.models.Tasks;
import com.jark.TMS.repo.TasksRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportsController {
    private final TasksRepository tasksRepository;

    public ReportsController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @GetMapping(value ="/api", produces = MediaType.APPLICATION_JSON_VALUE)
    //@ResponseBody
    public ResponseEntity<Iterable<Tasks>> reportsExecutor() {
        Iterable<Tasks> tasks = tasksRepository.findAll();
        return ResponseEntity.ok().body(tasks);
    }
}
