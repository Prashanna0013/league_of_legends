package com.example.lol.controller;

import com.example.lol.entity.Team;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/team")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add-team")
    public Team saveTeam(@RequestBody Team team){
        return teamService.saveTeam(team);
    }
    @GetMapping("/get-team")
    public Optional<List<Team>> getAllTeam(){
        return teamService.getAllTeams();
    }
    @GetMapping("get-id/{id}")
    public Optional<Team> getTeamById(@PathVariable Long id){
        return teamService.getTeamById(id);
    }
    @GetMapping("get-name/{name}")
    public Optional<Team> getTeamByName(@PathVariable String name){
        return teamService.getTeamByName(name);
    }

}
