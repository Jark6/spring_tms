package com.jark.TMS.controllers;

import com.jark.TMS.models.*;
import com.jark.TMS.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class TMSController {
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskTypeRepository taskTypeRepository;
    @Autowired
    private LinkedTaskTypeRepository linkedTaskTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PriorityRepository priorityRepository;


    @GetMapping("/tasks")
    public String tasksMain(Model model){
        Iterable<Tasks> tasks = tasksRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks-main";
    }

    @GetMapping("/tasks/add")
    public String taskAdd(Model model){
        Iterable<Status> statuses = statusRepository.findAll();
        model.addAttribute("statuses", statuses);
        Iterable<TaskType> taskTypes = taskTypeRepository.findAll();
        model.addAttribute("taskTypes", taskTypes);
        Iterable<Tasks> tasks = tasksRepository.findAll();
        model.addAttribute("linkedTasks", tasks);
        Iterable<LinkedTaskType> linkedTaskTypes = linkedTaskTypeRepository.findAll();
        model.addAttribute("linkedTaskTypes", linkedTaskTypes);
        Iterable<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        Iterable<Users> users = usersRepository.findAll();
        model.addAttribute("users", users);
        Iterable<Priority> priorities = priorityRepository.findAll();
        model.addAttribute("priorities", priorities);
        return "tasks-add";
    }

    @PostMapping("/tasks/add")
    public String taskPostAdd(@RequestParam(required=false) Long task_type_id, @RequestParam(required=false) Long status_id, @RequestParam String short_description,
                              @RequestParam String full_description, @RequestParam(required=false) Long linked_task_id,
                              @RequestParam(required=false) Long linked_task_type_id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                              @RequestParam(required=false) Long project_id, @RequestParam(required=false) Long executor_id,
                              @RequestParam(required=false) Long author_id, @RequestParam(required=false) Long priority_id, Model model){
        Tasks task = new Tasks(task_type_id, status_id, short_description, full_description, linked_task_id,
                linked_task_type_id, deadline, project_id, executor_id, author_id, priority_id);
        tasksRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{task_id}")
    public String taskDetails(@PathVariable(value = "task_id") long id, Model model){
        if(!tasksRepository.existsById(id)){
            return "redirect:/tasks";
        }
        Optional<Tasks> tasks = tasksRepository.findById(id);
/*        tasks.ifPresent(task -> {
            model.addAttribute("task", task);
            Status status = statusRepository.findById(task.getStatus_id()).orElse(null);
            model.addAttribute("status", status);
        });*/
        ArrayList <Tasks> res = new ArrayList<>();
        tasks.ifPresent(res::add);
        model.addAttribute("tasks", res);
        Optional<Status> status = statusRepository.findById(tasks.get().getStatus_id());
        model.addAttribute("status", status.orElse(null));
        return "tasks-details";
    }
/*
    @GetMapping("/tasks/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model){
        if(!tasksRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional <Tasks> tasks = tasksRepository.findById(id);
        ArrayList<Tasks> res = new ArrayList<>();
        tasks.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model){
        Post post = tasksRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        tasksRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model){
        Post post = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(post);
        return "redirect:/blog";
    }*/
}
