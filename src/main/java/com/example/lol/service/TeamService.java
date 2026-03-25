package com.example.lol.service;

import com.example.lol.entity.Team;
import com.example.lol.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team saveTeam(Team team){
        return teamRepository.save(team);
    }
    public Optional<List<Team>> getAllTeams(){
        return Optional.of(teamRepository.findAll());
    }
    public Optional<Team> getTeamById(Long id){
        return Optional.ofNullable(teamRepository.findById(id).orElse(null));
    }
    public Optional<Team> getTeamByName(String name){
        return Optional.ofNullable(teamRepository.findByName(name).orElse(null));
    }

}
