package com.jark.TMS.controllers;

import com.jark.TMS.repo.UsersRepository;
import com.jark.TMS.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TeamController {

    private final TeamService teamService;
    private final UsersRepository usersRepository;

    @Autowired
    public TeamController(TeamService teamService, UsersRepository usersRepository) {
        this.teamService = teamService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/teams/add")
    public String showTeamForm(Model model) {
        model.addAttribute("users", usersRepository.findAll());
        return "team-add";
    }

    @PostMapping("/teams/add")
    public String createTeam(@RequestParam String teamName, @RequestParam List<Long> userIds) {
        teamService.createTeamWithMembers(teamName, userIds);
        return "redirect:/about";
    }
}
