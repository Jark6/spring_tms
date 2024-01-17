package com.jark.TMS.controllers;

import com.jark.TMS.models.*;
import com.jark.TMS.repo.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class TMSController {
    private final TasksRepository tasksRepository;
    private final StatusRepository statusRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final LinkedTaskTypeRepository linkedTaskTypeRepository;
    private final ProjectRepository projectRepository;
    private final UsersRepository usersRepository;
    private final PriorityRepository priorityRepository;
    private final CommentsRepository commentsRepository;
    private RedirectAttributes redirectAttributes;

    public TMSController(TasksRepository tasksRepository, StatusRepository statusRepository, TaskTypeRepository taskTypeRepository,
                         LinkedTaskTypeRepository linkedTaskTypeRepository, ProjectRepository projectRepository,
                         UsersRepository usersRepository, PriorityRepository priorityRepository, CommentsRepository commentsRepository) {
        this.tasksRepository = tasksRepository;
        this.statusRepository = statusRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.linkedTaskTypeRepository = linkedTaskTypeRepository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
        this.priorityRepository = priorityRepository;
        this.commentsRepository = commentsRepository;
    }


    @GetMapping("/tasks")
    public String tasksMain(Model model){
        Iterable<Tasks> tasks = tasksRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks-main";
    }

    @GetMapping("/tasks/add")
    public String taskAdd(Model model){
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("taskTypes", taskTypeRepository.findAll());
        model.addAttribute("linkedTasks", tasksRepository.findAll());
        model.addAttribute("linkedTaskTypes", linkedTaskTypeRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("users", usersRepository.findAll());
        model.addAttribute("priorities", priorityRepository.findAll());
        return "tasks-add";
    }

    @PostMapping("/tasks/add")
    public String taskPostAdd(@RequestParam(required=false) TaskType task_type_id, @RequestParam(required=false) Status status_id, @RequestParam String short_description,
                              @RequestParam String full_description, @RequestParam(required=false) Long linked_task_id,
                              @RequestParam(required=false) LinkedTaskType linked_task_type_id, @RequestParam @NotNull(message = "Не указана дата") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                              @RequestParam(required=false) Project project_id, @RequestParam(required=false) Users executor_id,
                              @RequestParam(required=false) Users author_id, @RequestParam(required=false) Priority priority_id, RedirectAttributes redirectAttributes, Model model){
        Tasks task = new Tasks(task_type_id, status_id, short_description, full_description, linked_task_id,
                linked_task_type_id, deadline, project_id, executor_id, author_id, priority_id);
        tasksRepository.save(task);
        redirectAttributes.addFlashAttribute("successMessage", "Задача успешно добавлена!");
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{task_id}")
    public String taskDetails(@PathVariable(value = "task_id") long id, Model model){
        if(!tasksRepository.existsById(id)){
            return "redirect:/tasks";
        }
        /*ArrayList <Tasks> res = new ArrayList<>();
        tasks.ifPresent(res::add);
        model.addAttribute("tasks", res);*/
        Optional<Tasks> taskOptional = tasksRepository.findById(id);
        taskOptional.ifPresent(task -> {
            List<Comments> comments = commentsRepository.findByTask(task);

            model.addAttribute("tasks", task);
            model.addAttribute("comments", comments);
        });
        return "tasks-details";
    }

    @PostMapping("/tasks/{task_id}/addComment")
    public String addComment(@PathVariable(value = "task_id") long taskId,
                             @RequestParam String content,
                             Principal principal) {
        Optional<Tasks> taskOptional = tasksRepository.findById(taskId);
        if (taskOptional.isPresent() && principal != null) {
            Users user = usersRepository.findByLogin(principal.getName());
            if (user != null) {
            Tasks task = taskOptional.get();

            Comments comment = new Comments();
            comment.setContent(content);
            comment.setUser(user);
            comment.setTask(task);
            comment.setTimestamp(LocalDateTime.now());

            commentsRepository.save(comment);}

        } else {
            // Обработка случая, когда пользователь не найден
            System.out.println("ошибка");
        }
        return "redirect:/tasks/" + taskId;
    }

    @GetMapping("/tasks/{task_id}/edit")
    public String taskEdit(@PathVariable(value = "task_id") long id, Model model){
        if(!tasksRepository.existsById(id)){
            return "redirect:/tasks";
        }
        model.addAttribute("tasks", tasksRepository.findAll());
        model.addAttribute("taskTypes", taskTypeRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("linkedType", linkedTaskTypeRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("users", usersRepository.findAll());
        model.addAttribute("priorities", priorityRepository.findAll());
        Optional <Tasks> tasks = tasksRepository.findById(id);
        tasks.ifPresent(t -> model.addAttribute("task", t));
        return "tasks-edit";
    }

    @PostMapping("/tasks/{task_id}/edit")
    public String taskPostUpdate(@PathVariable(value = "task_id") Long id, @RequestParam(required=false) TaskType task_type_id,
                                 @RequestParam(required=false) Status status_id, @RequestParam String short_description,
                                 @RequestParam String full_description, @RequestParam(required=false) Long linked_task_id,
                                 @RequestParam(required=false) LinkedTaskType linked_task_type_id,
                                 @RequestParam @NotNull(message = "Не указана дата") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                                 @RequestParam(required=false) Project project_id, @RequestParam(required=false) Users executor_id,
                                 @RequestParam(required=false) Users author_id, @RequestParam(required=false) Priority priority_id, RedirectAttributes redirectAttributes,Model model)
    {
        Tasks post = tasksRepository.findById(id).orElseThrow();
        post.setTask_type_id(task_type_id);
        post.setStatus_id(status_id);
        post.setShort_description(short_description);
        post.setFull_description(full_description);
        post.setLinked_task_id(linked_task_id);
        post.setLinked_task_type_id(linked_task_type_id);
        post.setDeadline(deadline);
        post.setProject_id(project_id);
        post.setExecutor_id(executor_id);
        post.setAuthor_id(author_id);
        post.setPriority_id(priority_id);
        tasksRepository.save(post);
        redirectAttributes.addFlashAttribute("successMessage", "Задача успешно изменена!");
        return "redirect:/tasks";
    }

   @PostMapping("/tasks/{task_id}/remove")
    public String taskPostDelete(@PathVariable(value = "task_id") long id, RedirectAttributes redirectAttributes, Model model){
        Tasks task = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(task);
       redirectAttributes.addFlashAttribute("successMessage", "Задача успешно удалена!");
        return "redirect:/tasks";
    }
}
