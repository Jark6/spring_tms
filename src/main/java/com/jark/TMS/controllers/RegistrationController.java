package com.jark.TMS.controllers;

import com.jark.TMS.models.Users;
import com.jark.TMS.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UsersService usersService;

    @Autowired
    public RegistrationController(UsersService usersService) {
        this.usersService = usersService;
    }
    @GetMapping
    public String registrationForm(Model model){
        model.addAttribute("user", new Users());
        return "registration";
    }
    @PostMapping
    public String addUser(@ModelAttribute Users user, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes){
        try {
            usersService.saveUser(user, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно создан");
            return "redirect:/login";
        } catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/registration";
        }
    }
}
