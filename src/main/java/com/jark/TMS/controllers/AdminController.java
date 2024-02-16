package com.jark.TMS.controllers;

import com.jark.TMS.models.*;
import com.jark.TMS.repo.*;
import com.jark.TMS.services.TeamService;
import com.jark.TMS.services.UsersService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UsersRepository usersRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final StatusRepository statusRepository;
    private final ProjectRepository projectRepository;
    private final LinkedTaskTypeRepository linkedTaskTypeRepository;
    private final PriorityRepository priorityRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final TeamRepository teamRepository;
    private final TeamService teamService;


    public AdminController(UsersRepository usersRepository, TaskTypeRepository taskTypeRepository,
                           StatusRepository statusRepository, ProjectRepository projectRepository,
                           LinkedTaskTypeRepository linkedTaskTypeRepository, PriorityRepository priorityRepository,
                           UsersService usersService, TeamRepository teamRepository, TeamService teamService) {
        this.usersRepository = usersRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.statusRepository = statusRepository;
        this.projectRepository = projectRepository;
        this.linkedTaskTypeRepository = linkedTaskTypeRepository;
        this.priorityRepository = priorityRepository;
        this.usersService = usersService;
        this.teamRepository = teamRepository;
        this.teamService = teamService;
    }
    @GetMapping
    public String adminDashboard(Model model) {
        List<Users> userList = (List<Users>) usersRepository.findAll();
        List<TaskType> taskTypeList = (List<TaskType>) taskTypeRepository.findAll();
        List<Status> statusList = (List<Status>) statusRepository.findAll();
        List<Project> projectList = (List<Project>) projectRepository.findAll();
        List<LinkedTaskType> linkedTaskTypeList = (List<LinkedTaskType>) linkedTaskTypeRepository.findAll();
        List<Priority> priorityList = (List<Priority>) priorityRepository.findAll();
        List<Team> teamList = (List<Team>)  teamRepository.findAll();

        model.addAttribute("users", userList);
        model.addAttribute("taskTypes", taskTypeList);
        model.addAttribute("statuses", statusList);
        model.addAttribute("projects", projectList);
        model.addAttribute("linkedTaskTypes", linkedTaskTypeList);
        model.addAttribute("priorities", priorityList);
        model.addAttribute("teams", teamList);
        model.addAttribute("newUser", new Users()); // для добавления нового пользователя
        model.addAttribute("newTaskType", new TaskType()); // для добавления нового пользователя
        model.addAttribute("newStatus", new Status()); // для добавления нового статуса
        model.addAttribute("newProject", new Project()); // для добавления нового проекта
        model.addAttribute("newLinkedTaskType", new LinkedTaskType()); // для добавления нового типа связи
        model.addAttribute("newPriority", new Priority());// для добавления нового приоритета
        model.addAttribute("newTeam", new Team());// для добавления новой команды
        return "admin";
    }
    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") Users newUser, RedirectAttributes redirectAttributes) {
        // Добавление нового пользователя
        try {
            newUser.setTimestamp_create(LocalDateTime.now());
            usersService.saveUser(newUser);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно создан");
            return "redirect:/admin";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/addTaskType")
    public String addTaskType(@ModelAttribute("newTaskType") TaskType newTaskType, RedirectAttributes redirectAttributes) {
        // Добавление нового типа задач
        taskTypeRepository.save(newTaskType);
        redirectAttributes.addFlashAttribute("successMessage", "Тип задачи успешно добавлен");
        return "redirect:/admin";
    }

    @PostMapping("/addStatus")
    public String addStatus(@ModelAttribute("newStatus") Status newStatus, RedirectAttributes redirectAttributes) {
        // Добавление нового статуса
        statusRepository.save(newStatus);
        redirectAttributes.addFlashAttribute("successMessage", "Статус успешно добавлен");
        return "redirect:/admin";
    }

    @PostMapping("/addProject")
    public String addProject(@ModelAttribute("newProject") Project newProject, RedirectAttributes redirectAttributes) {
        // Добавление нового проекта
        projectRepository.save(newProject);
        redirectAttributes.addFlashAttribute("successMessage", "Проект успешно добавлен");
        return "redirect:/admin";
    }

    @PostMapping("/addLinkedTaskType")
    public String addLinkedTaskType(@ModelAttribute("newProject") LinkedTaskType newLinkedTaskType, RedirectAttributes redirectAttributes) {
        // Добавление нового типа связи
        linkedTaskTypeRepository.save(newLinkedTaskType);
        redirectAttributes.addFlashAttribute("successMessage", "Тип связи успешно добавлен");
        return "redirect:/admin";
    }

    @PostMapping("/addPriority")
    public String addPriority(@ModelAttribute("newPriority") Priority newPriority, RedirectAttributes redirectAttributes) {
        // Добавление нового приоритета
        priorityRepository.save(newPriority);
        redirectAttributes.addFlashAttribute("successMessage", "Приоритет успешно добавлен");
        return "redirect:/admin";
    }

    @PostMapping("/addTeam")
    public String addTeam(@ModelAttribute("newTeam") Team newTeam, RedirectAttributes redirectAttributes,
                          @RequestParam String teamName, @RequestParam List<Long> userIds) {
        // Добавление новой команды
        teamService.createTeamWithMembers(teamName, userIds);
        redirectAttributes.addFlashAttribute("successMessage", "Команда успешно добавлена");
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        // Удаление пользователя
        usersRepository.deleteById(userId);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален");
        return "redirect:/admin";
    }

    @GetMapping("/deleteTaskTypeId/{taskTypeId}")
    public String deleteTaskType(@PathVariable Long taskTypeId, RedirectAttributes redirectAttributes) {
        // Удаление типа задачи
        taskTypeRepository.deleteById(taskTypeId);
        redirectAttributes.addFlashAttribute("successMessage", "Тип задачи успешно удален");
        return "redirect:/admin";
    }

    @GetMapping("/deleteStatusId/{StatusId}")
    public String deleteStatus(@PathVariable Long StatusId, RedirectAttributes redirectAttributes) {
        // Удаление типа задачи
        statusRepository.deleteById(StatusId);
        redirectAttributes.addFlashAttribute("successMessage", "Статус успешно удален");
        return "redirect:/admin";
    }
    @GetMapping("/deleteProjectId/{ProjectId}")
    public String deleteProject(@PathVariable Long ProjectId, RedirectAttributes redirectAttributes) {
        // Удаление проекта
        projectRepository.deleteById(ProjectId);
        redirectAttributes.addFlashAttribute("successMessage", "Проект успешно удален");
        return "redirect:/admin";
    }

    @GetMapping("/deleteLinkedTaskTypeId/{LinkedTaskTypeId}")
    public String deleteLinkedTaskType(@PathVariable Long LinkedTaskTypeId, RedirectAttributes redirectAttributes) {
        // Удаление типа связи
        linkedTaskTypeRepository.deleteById(LinkedTaskTypeId);
        redirectAttributes.addFlashAttribute("successMessage", "Тип связи успешно удален");
        return "redirect:/admin";
    }

    @GetMapping("/deletePriorityId/{PriorityId}")
    public String deletePriority(@PathVariable Long PriorityId, RedirectAttributes redirectAttributes) {
        // Удаление приоритета
        priorityRepository.deleteById(PriorityId);
        redirectAttributes.addFlashAttribute("successMessage", "Приоритет успешно удален");
        return "redirect:/admin";
    }
    @GetMapping("/deleteTeamId/{TeamId}")
    public String deleteTeam(@PathVariable Long TeamId, RedirectAttributes redirectAttributes) {
        // Удаление команды
        teamRepository.deleteById(TeamId);
        redirectAttributes.addFlashAttribute("successMessage", "Команда успешно удалена");
        return "redirect:/admin";
    }
    @GetMapping("/editUserId/{UserId}")
    public String editUser(@PathVariable Long UserId, Model model) {
        // Получите информацию о пользователе
        Users user = usersRepository.findById(UserId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Передайте информацию о пользователе в модель
        model.addAttribute("editUserId", user);

        return "user-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editUserId")
    public String saveEditedUser(@ModelAttribute("editUserId") Users editedUser, RedirectAttributes redirectAttributes) {
        // Сохраните отредактированного пользователя
        Users originalUser = usersRepository.findById(editedUser.getUser_id())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        originalUser.setFirst_name(editedUser.getFirst_name());
        originalUser.setSecond_name(editedUser.getSecond_name());
        originalUser.setFamily_name(editedUser.getFamily_name());
        originalUser.setLogin(editedUser.getLogin());
        originalUser.setRole(editedUser.getRole());
        originalUser.setEmail(editedUser.getEmail());
        originalUser.setTimestamp_edit(LocalDateTime.now());
        usersRepository.save(originalUser);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно изменен");
        // Перенаправьте на страницу с таблицей типов задач
        return "redirect:/admin";
    }
    @PostMapping("/resetPassword/{UserId}")
    public String resetPassword(@PathVariable  Long UserId, RedirectAttributes redirectAttributes) {
        // Получите информацию о пользователе
        Users user = usersRepository.findById(UserId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setPasswordHash(user.getLogin());
        user.setTimestamp_edit(LocalDateTime.now());
        usersRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Пароль сброшен");
        return "redirect:/admin";
    }

    @GetMapping("/editTaskTypeId/{taskTypeId}")
    public String editTaskType(@PathVariable Long taskTypeId, Model model) {
        // Получите информацию о типе задачи по taskTypeId
        TaskType taskType = taskTypeRepository.findById(taskTypeId)
                .orElseThrow(() -> new RuntimeException("Тип задачи не найден"));

        // Передайте информацию о типе задачи в модель
        model.addAttribute("editTaskType", taskType);

        return "task-type-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editTaskType")
    public String saveEditedTaskType(@ModelAttribute("editTaskType") TaskType editedTaskType, RedirectAttributes redirectAttributes) {
        // Сохраните отредактированный тип задачи
        taskTypeRepository.save(editedTaskType);
        redirectAttributes.addFlashAttribute("successMessage", "Тип задачи успешно изменен");
        // Перенаправьте на страницу с таблицей типов задач
        return "redirect:/admin";
    }

    @GetMapping("/editStatusId/{StatusId}")
    public String editStatus(@PathVariable Long StatusId, Model model) {
        // Получите информацию о статусе по StatusId
        Status status = statusRepository.findById(StatusId)
                .orElseThrow(() -> new RuntimeException("Статус не найден"));

        // Передайте информацию о статусе в модель
        model.addAttribute("editStatus", status);

        return "status-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editStatus")
    public String saveEditedStatus(@ModelAttribute("editStatus") Status editedStatus, RedirectAttributes redirectAttributes) {
        // Сохраните отредактированный статус
        statusRepository.save(editedStatus);
        redirectAttributes.addFlashAttribute("successMessage", "Статус успешно изменен");
        // Перенаправьте на страницу с таблицей статусов
        return "redirect:/admin";
    }

    @GetMapping("/editProjectId/{ProjectId}")
    public String editProject(@PathVariable Long ProjectId, Model model) {
        // Получите информацию о проекте по ProjectId
        Project project = projectRepository.findById(ProjectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        // Передайте информацию о проекте в модель
        model.addAttribute("editProject", project);

        return "project-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editProject")
    public String saveEditedProject(@ModelAttribute("editProject") Project editedProject, RedirectAttributes redirectAttributes) {
        // Сохраните отредактированный проект
        projectRepository.save(editedProject);
        redirectAttributes.addFlashAttribute("successMessage", "Проект успешно изменен");
        // Перенаправьте на страницу с таблицей проектоа
        return "redirect:/admin";
    }

    @GetMapping("/editLinkedTaskTypeId/{LinkedTaskTypeId}")
    public String editLinkedTaskType(@PathVariable Long LinkedTaskTypeId, Model model) {
        // Получите информацию о типе связи задачи по LinkedTaskTypeId
        LinkedTaskType linkedTaskType = linkedTaskTypeRepository.findById(LinkedTaskTypeId)
                .orElseThrow(() -> new RuntimeException("Тип связи с задачей не найден"));

        // Передайте информацию о типе связи с задачей в модель
        model.addAttribute("editLinkedTaskType", linkedTaskType);

        return "linked-task-type-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editLinkedTaskType")
    public String saveEditedLinkedTaskType(@ModelAttribute("editLinkedTaskType") LinkedTaskType editedLinkedTaskType, RedirectAttributes redirectAttributes) {
        // Сохраните отредактированный тип связи с задачей
        linkedTaskTypeRepository.save(editedLinkedTaskType);
        redirectAttributes.addFlashAttribute("successMessage", "Тип связи с задачей успешно изменен");
        // Перенаправьте на страницу с таблицей типов задач
        return "redirect:/admin";
    }

    @GetMapping("/editPriorityId/{PriorityId}")
    public String editPriority(@PathVariable Long PriorityId, Model model) {
        // Получите информацию о проекте по PriorityId
        Priority priority = priorityRepository.findById(PriorityId)
                .orElseThrow(() -> new RuntimeException("Приоритет не найден"));

        // Передайте информацию о приоритете в модель
        model.addAttribute("editPriority", priority);

        return "priority-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editPriority")
    public String saveEditedPriority(@ModelAttribute("editPriority") Priority editedPriority, RedirectAttributes redirectAttributes) {
        // Сохраните отредактированный приоритет
        priorityRepository.save(editedPriority);
        redirectAttributes.addFlashAttribute("successMessage", "Приоритет успешно изменен");
        // Перенаправьте на страницу с таблицей проектоа
        return "redirect:/admin";
    }

    @GetMapping("/editTeamId/{TeamId}")
    public String editTeam(@PathVariable Long TeamId, Model model) {
        // Получение информации о team по TeamId
        Team team = teamRepository.findById(TeamId)
                .orElseThrow(() -> new RuntimeException("Команда не найдена"));

        // Передача информации о команде и ее членах в модель
        model.addAttribute("editTeam", team);
        model.addAttribute("members", team.getMembers());
        model.addAttribute("users", usersRepository.findAll());
        return "team-edit"; // Название шаблона для страницы редактирования
    }

    @PostMapping("/editTeam")
    public String saveEditedTeam(@ModelAttribute("editTeam") Team editedTeam, RedirectAttributes redirectAttributes,
                                 @RequestParam String team_name, @RequestParam List<Long> userIds) {
        // Обновляем данные команды
        teamService.updateTeam(editedTeam.getTeam_id(), team_name, userIds);
        redirectAttributes.addFlashAttribute("successMessage", "Команда успешно изменена");
        // Перенаправление на страницу с таблицей проектов
        return "redirect:/admin";
    }


}
