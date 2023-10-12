package com.jark.TMS.controllers;

import com.jark.TMS.models.Status;
import com.jark.TMS.models.Tasks;
import com.jark.TMS.repo.StatusRepository;
import com.jark.TMS.repo.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class TMSController {
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private StatusRepository statusRepository;
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
        return "tasks-add";
    }

    @PostMapping("/tasks/add")
    public String taskPostAdd(@RequestParam Long task_type_id, @RequestParam Long status_id, @RequestParam String short_description,
                              @RequestParam String full_description, @RequestParam Long linked_task_id,
                              @RequestParam Long linked_task_type_id, @RequestParam Date deadline,
                              @RequestParam Long project_id, @RequestParam Long executor_id,
                              @RequestParam Long author_id, @RequestParam Long priority_id, Model model){
        Tasks task = new Tasks(task_type_id, status_id, short_description, full_description, linked_task_id,
                linked_task_type_id, deadline, project_id, executor_id, author_id, priority_id);
        tasksRepository.save(task);
        return "redirect:/tasks";
    }

    /*@GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model){
        if(!tasksRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional <Post> post = tasksRepository.findById(id);
        ArrayList <Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model){
        if(!tasksRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional <Post> post = tasksRepository.findById(id);
        ArrayList <Post> res = new ArrayList<>();
        post.ifPresent(res::add);
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