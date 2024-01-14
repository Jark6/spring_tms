package com.jark.TMS.controllers;

import com.jark.TMS.models.Users;
import com.jark.TMS.repo.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UsersRepository usersRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @GetMapping
    public String adminDashboard(Model model) {
        List<Users> userList = (List<Users>) usersRepository.findAll();
        model.addAttribute("users", userList);
        model.addAttribute("newUser", new Users()); // для добавления нового пользователя
        return "admin";
    }
    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") Users newUser, RedirectAttributes redirectAttributes) {
        // Добавление нового пользователя
        newUser.setPassword(passwordEncoder.encode(newUser.getPasswordHash())); // Пример
        usersRepository.save(newUser);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно добавлен");
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        // Удаление пользователя
        usersRepository.deleteById(userId);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален");
        return "redirect:/admin";
    }
}
