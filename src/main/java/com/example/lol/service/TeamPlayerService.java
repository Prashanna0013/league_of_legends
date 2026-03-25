package com.example.lol.service;

import com.example.lol.entity.TeamPlayer;
import com.example.lol.repository.TeamPlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamPlayerService {
    private final TeamPlayerRepository teamPlayerRepository;
    private final TeamService teamService;
    private final PlayerService playerService;

    public TeamPlayerService(TeamPlayerRepository teamPlayerRepository,
                             TeamService teamService,
                             PlayerService playerService) {
        this.teamPlayerRepository = teamPlayerRepository;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    public TeamPlayer saveTeamPlayer(TeamPlayer teamPlayer){
        return teamPlayerRepository.save(teamPlayer);
    }

    public boolean checkTeamAndPlayer(Long playerId, Long teamId){
        return teamService.getTeamById(teamId).isPresent() && playerService.getPlayerById(playerId).isPresent();
    }
}
