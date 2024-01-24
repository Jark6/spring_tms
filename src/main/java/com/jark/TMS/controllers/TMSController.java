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
import java.util.*;

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
    @GetMapping("/tasks/table")
    public String tasksTable(Model model){
        Iterable<Tasks> tasks = tasksRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks-table";
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
        LocalDateTime timestampCreate = LocalDateTime.now();
        Tasks task = new Tasks(task_type_id, status_id, short_description, full_description, linked_task_id,
                linked_task_type_id, deadline, project_id, executor_id, author_id, priority_id, timestampCreate, null);
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
            commentsRepository.save(comment);
            task.setTimestamp_edit(LocalDateTime.now());
            tasksRepository.save(task);
            }

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
    public String taskPostUpdate(@PathVariable(value = "task_id") Long id,
                                 @RequestParam(required = false) TaskType task_type_id,
                                 @RequestParam(required = false) Status status_id,
                                 @RequestParam String short_description,
                                 @RequestParam String full_description,
                                 @RequestParam(required = false) Long linked_task_id,
                                 @RequestParam(required = false) LinkedTaskType linked_task_type_id,
                                 @RequestParam @NotNull(message = "Не указана дата") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                                 @RequestParam(required = false) Project project_id,
                                 @RequestParam(required = false) Users executor_id,
                                 @RequestParam(required = false) Users author_id,
                                 @RequestParam(required = false) Priority priority_id,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Principal principal) {

        // Находим существующую задачу в базе данных
        Tasks existingTask = tasksRepository.findById(id).orElseThrow();

        // Создаем объект для хранения измененных полей
        Map<String, String> changedFields = new HashMap<>();

        // Сравниваем новые значения с существующими значениями
        compareAndAddChange(changedFields, "Тип задачи", existingTask.getTask_type_id(), task_type_id);
        compareAndAddChange(changedFields, "Статус", existingTask.getStatus_id(), status_id);
        compareAndAddChange(changedFields, "Краткое описание", existingTask.getShort_description(), short_description);
        compareAndAddChange(changedFields, "Полное описание", existingTask.getFull_description(), full_description);
        compareAndAddChange(changedFields, "Связанная задача", existingTask.getLinked_task_id(), linked_task_id);
        compareAndAddChange(changedFields, "Тип связи", existingTask.getLinked_task_type_id(), linked_task_type_id);
        compareAndAddChange(changedFields, "Срок выполнения", existingTask.getDeadline(), deadline);
        compareAndAddChange(changedFields, "Проект", existingTask.getProject_id(), project_id);
        compareAndAddChange(changedFields, "Автор", existingTask.getAuthor_id(), author_id);
        compareAndAddChange(changedFields, "Исполнитель", existingTask.getExecutor_id(), executor_id);

        // Обновляем задачу
        existingTask.setTask_type_id(task_type_id);
        existingTask.setStatus_id(status_id);
        existingTask.setShort_description(short_description);
        existingTask.setFull_description(full_description);
        existingTask.setLinked_task_id(linked_task_id);
        existingTask.setLinked_task_type_id(linked_task_type_id);
        existingTask.setDeadline(deadline);
        existingTask.setProject_id(project_id);
        existingTask.setAuthor_id(author_id);
        existingTask.setExecutor_id(executor_id);

        // Сохраняем измененную задачу
        tasksRepository.save(existingTask);

        // Добавляем комментарии о изменениях
        if (!changedFields.isEmpty() && principal != null) {
            StringBuilder commentContent = new StringBuilder("Изменения в задаче:</br>");
            for (Map.Entry<String, String> entry : changedFields.entrySet()) {
                commentContent.append(entry.getKey())
                        .append(": изменено '").append(entry.getValue()).append("' на '").append(getValueAsString(existingTask, entry.getKey())).append("</br>");
            }

            // Создаем комментарий
            Comments comment = new Comments();
            comment.setContent(commentContent.toString());
            comment.setUser(usersRepository.findByLogin(principal.getName()));
            comment.setTask(existingTask);
            comment.setTimestamp(LocalDateTime.now());
            commentsRepository.save(comment);

            // Обновляем метку времени последнего изменения задачи
            existingTask.setTimestamp_edit(LocalDateTime.now());
            tasksRepository.save(existingTask);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Задача успешно изменена!");
        return "redirect:/tasks";
    }

    // Метод для сравнения значений и добавления измененных полей в Map
    private <T> void compareAndAddChange(Map<String, String> changedFields, String fieldName, T oldValue, T newValue) {
        if (oldValue == null && newValue != null || (oldValue != null && !oldValue.equals(newValue))) {
            changedFields.put(fieldName, oldValue != null ? oldValue.toString() : "null");
        }
    }

    // Метод для получения значения поля в виде строки
    private String getValueAsString(Tasks task, String fieldName) {
        switch (fieldName) {
            case "Тип задачи":
                return task.getTask_type_id() != null ? task.getTask_type_id().getTask_type() : "";
            case "Статус":
                return task.getStatus_id() != null ? task.getStatus_id().getStatus() : "";
            case "Краткое описание":
                return task.getShort_description();
            case "Полное описание":
                return task.getFull_description();
            case "Связанная задача":
                return task.getLinked_task_id() != null ? task.getLinked_task_id().toString() : "";
            case "Тип связи":
                return task.getLinked_task_type_id() != null ? task.getLinked_task_type_id().getLinked_task_type() : "";
            case "Срок выполнения":
                return task.getDeadline().toString();
            case "Проект":
                return task.getProject_id() != null ? task.getProject_id().getProject_name() : "";
            case "Автор":
                return task.getAuthor_id() != null ? task.getAuthor_id().getSecond_name() +" "+ task.getAuthor_id().getFirst_name() : "";
            case "Исполнитель":
                return task.getExecutor_id() != null ? task.getExecutor_id().getSecond_name() +" "+ task.getExecutor_id().getFirst_name() : "";
            default:
                return "";
        }
    }

   @PostMapping("/tasks/{task_id}/remove")
    public String taskPostDelete(@PathVariable(value = "task_id") long id, RedirectAttributes redirectAttributes, Model model){
        Tasks task = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(task);
       redirectAttributes.addFlashAttribute("successMessage", "Задача успешно удалена!");
        return "redirect:/tasks";
    }
}
