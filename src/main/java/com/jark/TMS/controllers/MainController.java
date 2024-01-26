package com.jark.TMS.controllers;

import com.jark.TMS.TmsLogger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private static final TmsLogger logger = new TmsLogger(MainController.class);
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        logger.logInfo("Visited home page");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Страница про нас");
        logger.logInfo("Visited about page");
        return "about";
    }
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("title", "Отчеты по задачам");
        logger.logInfo("Visited reports page");
        return "reports";
    }
    @GetMapping("/reports/executor")
    public String reportsExecutor(Model model) {
        model.addAttribute("title", "Отчеты по исполнителям");
        logger.logInfo("Visited reports/executor page");
        return "reports-executor";
    }

    @GetMapping("/reports/author")
    public String reportsAuthor(Model model) {
        model.addAttribute("title", "Отчеты по авторам");
        logger.logInfo("Visited reports/author page");
        return "reports-author";
    }

    @GetMapping("/reports/tasks")
    public String reportsTasks(Model model) {
        model.addAttribute("title", "Отчеты по задачам");
        logger.logInfo("Visited reports/tasks page");
        return "reports-tasks";
    }

    @GetMapping("/gannt")
    public String gannt(Model model) {
        model.addAttribute("title", "Диаграмма Ганнта");
        logger.logInfo("Visited gannt page");
        return "gannt-chart";
    }
}
