package com.example.lol.controller;

import com.example.lol.entity.TeamPlayer;
import com.example.lol.service.TeamPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teamplayer")
public class TeamPlayerController {
    @Autowired
    private TeamPlayerService teamPlayerService;

    @PostMapping("/assign")
    public String saveTeamPlayer(@RequestBody TeamPlayer teamPlayer){
        Long playerId = teamPlayer.getPlayerId();
        Long teamId = teamPlayer.getTeamId();
        if(teamPlayerService.checkTeamAndPlayer(playerId, teamId)){
            teamPlayerService.saveTeamPlayer(teamPlayer);
            return "Player assigned to team successfully.";
        } else {
            return "Invalid player ID or team ID.";
        }
    }
}
