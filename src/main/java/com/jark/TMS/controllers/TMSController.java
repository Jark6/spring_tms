package com.jark.TMS.controllers;

import com.jark.TMS.models.*;
import com.jark.TMS.repo.*;
import com.jark.TMS.services.EmailService;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final EmailService emailService;
    private final TasksRepository tasksRepository;
    private final StatusRepository statusRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final LinkedTaskTypeRepository linkedTaskTypeRepository;
    private final ProjectRepository projectRepository;
    private final UsersRepository usersRepository;
    private final PriorityRepository priorityRepository;
    private final CommentsRepository commentsRepository;

    public TMSController(TasksRepository tasksRepository, StatusRepository statusRepository, TaskTypeRepository taskTypeRepository,
                         LinkedTaskTypeRepository linkedTaskTypeRepository, ProjectRepository projectRepository,
                         UsersRepository usersRepository, PriorityRepository priorityRepository, CommentsRepository commentsRepository, EmailService emailService) {
        this.tasksRepository = tasksRepository;
        this.statusRepository = statusRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.linkedTaskTypeRepository = linkedTaskTypeRepository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
        this.priorityRepository = priorityRepository;
        this.commentsRepository = commentsRepository;
        this.emailService = emailService;
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
                              @RequestParam(required=false) Users author_id, @RequestParam(required=false) Priority priority_id, RedirectAttributes redirectAttributes){
        LocalDateTime timestampCreate = LocalDateTime.now();
        Tasks task = new Tasks(task_type_id, status_id, short_description, full_description, linked_task_id,
                linked_task_type_id, deadline, project_id, executor_id, author_id, priority_id, timestampCreate, null);
        tasksRepository.save(task);
        redirectAttributes.addFlashAttribute("successMessage", "Задача успешно добавлена!");
        emailService.sendEmail(author_id.getEmail(), "создана задача", "Здравсвуйте, \nСоздана новая задача "+short_description+" .\n");
        if (author_id != executor_id)
            emailService.sendEmail(executor_id.getEmail(), "создана задача", "Здравсвуйте, \nСоздана новая задача " + short_description + " .\n");
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

                emailService.sendEmail(task.getAuthor_id().getEmail(), "комментарий к задаче \""+task.getShort_description()+"\"",
                        "Пользователь "+user.getSecond_name()+" "+user.getFirst_name()+" добавил комментарий:\n"+comment.getContent());
                if (task.getAuthor_id() != task.getExecutor_id())
                    emailService.sendEmail(task.getExecutor_id().getEmail(),  "комментарий к задаче \""+task.getShort_description()+"\"",
                            "Пользователь "+user.getSecond_name()+" "+user.getFirst_name()+" добавил комментарий:\n"+comment.getContent());
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
                                 Principal principal) {

        // Находим существующую задачу в базе данных
        Tasks existingTask = tasksRepository.findById(id).orElseThrow();

        // Создаем объект для хранения измененных полей
        Map<String, String> changedFields = new HashMap<>();

        // Сравниваем новые значения с существующими значениями
        compareAndAddChange(changedFields, "Тип задачи", existingTask.getTask_type_id(),
                existingTask.getTask_type_id() !=null ? existingTask.getTask_type_id().getTask_type():"", task_type_id);
        compareAndAddChange(changedFields, "Статус", existingTask.getStatus_id(),
                existingTask.getStatus_id()!=null ? existingTask.getStatus_id().getStatus() : "", status_id);
        compareAndAddChange(changedFields, "Краткое описание", existingTask.getShort_description(), existingTask.getShort_description(), short_description);
        compareAndAddChange(changedFields, "Полное описание", existingTask.getFull_description(), existingTask.getFull_description(), full_description);
        compareAndAddChange(changedFields, "Связанная задача", existingTask.getLinked_task_id(),existingTask.getLinked_task_id(), linked_task_id);
        compareAndAddChange(changedFields, "Тип связи", existingTask.getLinked_task_type_id(),
                existingTask.getLinked_task_type_id() !=null ? existingTask.getLinked_task_type_id().getLinked_task_type() : "", linked_task_type_id);
        compareAndAddChange(changedFields, "Срок выполнения", existingTask.getDeadline(), existingTask.getDeadline(), deadline);
        compareAndAddChange(changedFields, "Приоритет", existingTask.getPriority_id(),
                existingTask.getPriority_id() != null ? existingTask.getPriority_id().getPriority() : "", priority_id);
        compareAndAddChange(changedFields, "Проект", existingTask.getProject_id(),
                existingTask.getProject_id() != null ? existingTask.getProject_id().getProject_name() : "", project_id);
        compareAndAddChange(changedFields, "Автор", existingTask.getAuthor_id(),
                existingTask.getAuthor_id() !=null ? existingTask.getAuthor_id().getFullName() : "", author_id);
        compareAndAddChange(changedFields, "Исполнитель", existingTask.getExecutor_id(),
                existingTask.getAuthor_id() !=null ? existingTask.getExecutor_id().getFullName() : "", executor_id);

        // Обновляем задачу
        existingTask.setTask_type_id(task_type_id);
        existingTask.setStatus_id(status_id);
        existingTask.setShort_description(short_description);
        existingTask.setFull_description(full_description);
        existingTask.setLinked_task_id(linked_task_id);
        existingTask.setLinked_task_type_id(linked_task_type_id);
        existingTask.setDeadline(deadline);
        existingTask.setPriority_id(priority_id);
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

            //Отправляем e-mail пользователям
            emailService.sendEmail(author_id.getEmail(), "изменения в задаче \""+short_description+"\"",
                    "Пользователь "+principal.getName()+" сделал следующие изменения:\n"+comment.getContent());
            if (author_id != executor_id)
                emailService.sendEmail(executor_id.getEmail(),  "изменения в задаче \""+short_description+"\"",
                        "Пользователь "+principal.getName()+" сделал следующие изменения:\n"+comment.getContent());

            // Обновляем метку времени последнего изменения задачи
            existingTask.setTimestamp_edit(LocalDateTime.now());
            tasksRepository.save(existingTask);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Задача успешно изменена!");
        return "redirect:/tasks";
    }

    // Метод для сравнения значений и добавления измененных полей в Map
    private <T> void compareAndAddChange(Map<String, String> changedFields, String fieldName, T oldValue, T oldConvertedValue, T newValue) {
        if (oldValue == null && newValue != null || (oldValue != null && !oldValue.equals(newValue))) {
            changedFields.put(fieldName, oldValue != null ? oldConvertedValue.toString() : "null");
        }
    }

    // Метод для получения значения поля в виде строки
    private String getValueAsString(Tasks task, String fieldName) {
        return switch (fieldName) {
            case "Тип задачи" -> task.getTask_type_id() != null ? task.getTask_type_id().getTask_type() : "";
            case "Статус" -> task.getStatus_id() != null ? task.getStatus_id().getStatus() : "";
            case "Краткое описание" -> task.getShort_description();
            case "Полное описание" -> task.getFull_description();
            case "Связанная задача" -> task.getLinked_task_id() != null ? task.getLinked_task_id().toString() : "";
            case "Тип связи" ->
                    task.getLinked_task_type_id() != null ? task.getLinked_task_type_id().getLinked_task_type() : "";
            case "Срок выполнения" -> task.getDeadline().toString();
            case "Приоритет" -> task.getPriority_id() != null ? task.getPriority_id().getPriority() : "";
            case "Проект" -> task.getProject_id() != null ? task.getProject_id().getProject_name() : "";
            case "Автор" ->
                    task.getAuthor_id() != null ? task.getAuthor_id().getSecond_name() + " " + task.getAuthor_id().getFirst_name() : "";
            case "Исполнитель" ->
                    task.getExecutor_id() != null ? task.getExecutor_id().getSecond_name() + " " + task.getExecutor_id().getFirst_name() : "";
            default -> "";
        };
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tasks/{task_id}/remove")
    public String taskPostDelete(@PathVariable(value = "task_id") long id, RedirectAttributes redirectAttributes){
        Tasks task = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(task);
       redirectAttributes.addFlashAttribute("successMessage", "Задача успешно удалена!");
        return "redirect:/tasks";
    }
}
