package com.example.lol.controller;

import com.example.lol.dto.team.AuctionAssignRequest;
import com.example.lol.dto.team.CreateTeamRequest;
import com.example.lol.dto.team.TeamPlayerDTO;
import com.example.lol.entity.Role;
import com.example.lol.entity.Team;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.security.CurrentUserService;
import com.example.lol.service.TeamManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamAuctionController {
    private final TeamManagementService teamManagementService;
    private final CurrentUserService currentUserService;

    public TeamAuctionController(TeamManagementService teamManagementService, CurrentUserService currentUserService) {
        this.teamManagementService = teamManagementService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/teams")
    public ResponseEntity<Team> createTeam(@RequestBody CreateTeamRequest request) {
        Role role = currentUserService.getCurrentRole();
        Team team = teamManagementService.createTeam(role, request.name, request.ownerId, request.budget);
        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }

    @PostMapping("/auction/assign")
    public ResponseEntity<TeamPlayer> assign(@RequestBody AuctionAssignRequest request) {
        Role role = currentUserService.getCurrentRole();
        Long userId = currentUserService.getCurrentUserId();
        TeamPlayer tp = teamManagementService.assignPlayer(role, userId, request.teamId, request.playerId, request.bidAmount);
        return ResponseEntity.status(HttpStatus.CREATED).body(tp);
    }

    @PostMapping("/teams/{teamId}/lock")
    public ResponseEntity<Void> lock(@PathVariable("teamId") Long teamId) {
        Role role = currentUserService.getCurrentRole();
        teamManagementService.lockTeam(role, teamId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teams/{teamId}/players")
    public List<TeamPlayerDTO> teamPlayers(@PathVariable("teamId") Long teamId) {
        Role role = currentUserService.getCurrentRole();
        Long userId = currentUserService.getCurrentUserId();

        List<TeamManagementService.TeamPlayerView> views = teamManagementService.getTeamPlayers(role, userId, teamId);
        return views.stream().map(v -> {
            TeamPlayerDTO dto = new TeamPlayerDTO();
            dto.teamId = v.teamId;
            dto.playerId = v.playerId;
            dto.playerName = v.playerName;
            dto.skillLevel = v.skillLevel;
            dto.assignedPrice = v.assignedPrice;
            return dto;
        }).toList();
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleForbidden(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}

