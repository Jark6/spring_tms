package com.jark.TMS.controllers;

import com.jark.TMS.models.Tasks;
import com.jark.TMS.repo.TasksRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Страница про нас");
        return "about";
    }
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("title", "Отчеты по задачам");
        return "reports";
    }
    @GetMapping("/reports/executor")
    public String reportsExecutor(Model model) {
        model.addAttribute("title", "Отчеты по исполнителям");
        return "reports-executor";
    }

    @GetMapping("/reports/author")
    public String reportsAuthor(Model model) {
        model.addAttribute("title", "Отчеты по авторам");
        return "reports-author";
    }

    @GetMapping("/reports/tasks")
    public String reportsTasks(Model model) {
        model.addAttribute("title", "Отчеты по задачам");
        return "reports-tasks";
    }

    @GetMapping("/gannt")
    public String gannt(Model model) {
        model.addAttribute("title", "Диаграмма Ганнта");
        return "gannt-chart";
    }
}
