package com.example.lol.service;

import com.example.lol.entity.Player;
import com.example.lol.entity.Role;
import com.example.lol.entity.Team;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.repository.PlayerRepository;
import com.example.lol.repository.TeamPlayerRepository;
import com.example.lol.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    public AuctionService(TeamRepository teamRepository, PlayerRepository playerRepository, TeamPlayerRepository teamPlayerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.teamPlayerRepository = teamPlayerRepository;
    }

    public TeamPlayer assignPlayerToTeam(Long actingUserId, Role actingRole, Long teamId, Long playerId, int bidAmount) {
        if (teamId == null || playerId == null) throw new IllegalArgumentException("teamId/playerId are required");
        if (bidAmount <= 0) throw new IllegalArgumentException("bidAmount must be > 0");

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));

        if (actingRole != Role.ADMIN && actingRole != Role.TEAM_OWNER) {
            throw new SecurityException("Only ADMIN or TEAM_OWNER can bid/assign players.");
        }
        if (actingRole == Role.TEAM_OWNER) {
            if (actingUserId == null) throw new SecurityException("actingUserId is required for TEAM_OWNER actions");
            if (team.getOwnerId() == null || !team.getOwnerId().equals(actingUserId)) {
                throw new SecurityException("TEAM_OWNER is not the owner of this team.");
            }
        }

        if (team.isLocked()) {
            throw new IllegalStateException("Team is locked. Bidding/assignment is not allowed.");
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        if (bidAmount < player.getBasePrice()) {
            throw new IllegalArgumentException("bidAmount must be >= player's base price (" + player.getBasePrice() + ")");
        }

        // Prevent duplicate assignment (a player assigned to multiple teams)
        if (teamPlayerRepository.existsByPlayerId(playerId)) {
            TeamPlayer existing = teamPlayerRepository.findByPlayerId(playerId)
                    .orElseThrow();
            if (!existing.getTeamId().equals(teamId)) {
                throw new IllegalStateException("Player is already assigned to another team.");
            }
            throw new IllegalStateException("Player is already assigned to this team.");
        }

        // Prevent duplicate assignment within the team (extra guard even though we also have a DB unique constraint)
        if (teamPlayerRepository.existsByTeamIdAndPlayerId(teamId, playerId)) {
            throw new IllegalStateException("Player already exists in this team.");
        }

        int currentSpent = sumAssignedPrice(teamId);
        if (currentSpent + bidAmount > team.getBudget()) {
            throw new IllegalStateException("Budget exceeded. Current spent=" + currentSpent + ", bid=" + bidAmount + ", budget=" + team.getBudget());
        }

        TeamPlayer teamPlayer = new TeamPlayer(null, teamId, playerId, bidAmount);
        return teamPlayerRepository.save(teamPlayer);
    }

    public void lockTeam(Long teamId, Role actingRole) {
        if (teamId == null) throw new IllegalArgumentException("teamId is required");
        if (actingRole != Role.ADMIN) {
            throw new SecurityException("Only ADMIN can lock teams.");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));

        team.setLocked(true);
        teamRepository.save(team);
    }

    private int sumAssignedPrice(Long teamId) {
        List<TeamPlayer> assigned = teamPlayerRepository.findByTeamId(teamId);
        int sum = 0;
        for (TeamPlayer tp : assigned) {
            sum += tp.getAssignedPrice();
        }
        return sum;
    }
}

