package com.jark.TMS.services;

import com.jark.TMS.models.Team;
import com.jark.TMS.models.Users;
import com.jark.TMS.repo.TeamRepository;
import com.jark.TMS.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UsersRepository usersRepository) {
        this.teamRepository = teamRepository;
        this.usersRepository = usersRepository;
    }

    //создание команды
    public void createTeamWithMembers(String teamName, List<Long> userIds) {
        Team team = new Team();
        team.setTeam_name(teamName);

        List<Users> members = StreamSupport.stream(usersRepository.findAllById(userIds).spliterator(), false)
                .collect(Collectors.toList());

        team.setMembers(members);

        teamRepository.save(team);
    }

    // Метод для обновления команды
    public void updateTeam(Long teamId, String teamName, List<Long> userIds) {
        // Здесь вы можете загрузить команду по ее ID
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Команда с ID " + teamId + " не найдена"));

        // Обновляем данные команды
        team.setTeam_name(teamName);
        // Обновляем список пользователей в команде
        List<Users> members = (List<Users>) usersRepository.findAllById(userIds);
        System.out.println("Members from repository: " + members);
        team.setMembers(members);

        // Сохраняем обновленную команду
        teamRepository.save(team);
    }
    }

