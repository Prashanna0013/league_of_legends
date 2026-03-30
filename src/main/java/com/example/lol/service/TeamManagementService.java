package com.example.lol.service;

import com.example.lol.entity.Player;
import com.example.lol.entity.Role;
import com.example.lol.entity.Team;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.repository.PlayerRepository;
import com.example.lol.repository.TeamPlayerRepository;
import com.example.lol.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamManagementService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final AuctionService auctionService;

    public TeamManagementService(
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            TeamPlayerRepository teamPlayerRepository,
            AuctionService auctionService
    ) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.auctionService = auctionService;
    }

    public Team createTeam(Role actingRole, String name, Long ownerId, int budget) {
        requireAdmin(actingRole);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("team name is required");
        if (ownerId == null) throw new IllegalArgumentException("ownerId is required");
        if (budget < 0) throw new IllegalArgumentException("budget must be >= 0");

        Team team = new Team();
        team.setName(name.trim());
        team.setOwnerId(ownerId);
        team.setBudget(budget);
        team.setLocked(false);
        return teamRepository.save(team);
    }

    public void lockTeam(Role actingRole, Long teamId) {
        auctionService.lockTeam(teamId, actingRole);
    }

    public TeamPlayer assignPlayer(Role actingRole, Long actingUserId, Long teamId, Long playerId, int bidAmount) {
        return auctionService.assignPlayerToTeam(actingUserId, actingRole, teamId, playerId, bidAmount);
    }

    public List<TeamPlayerView> getTeamPlayers(Role actingRole, Long actingUserId, Long teamId) {
        if (teamId == null) throw new IllegalArgumentException("teamId is required");

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));

        if (actingRole == Role.TEAM_OWNER) {
            if (actingUserId == null || team.getOwnerId() == null || !team.getOwnerId().equals(actingUserId)) {
                throw new SecurityException("TEAM_OWNER is not the owner of this team.");
            }
        } else if (actingRole != Role.ADMIN) {
            throw new SecurityException("Not allowed to view team players.");
        }

        List<TeamPlayer> mappings = teamPlayerRepository.findByTeamId(teamId);
        List<TeamPlayerView> result = new ArrayList<>();
        for (TeamPlayer tp : mappings) {
            Player player = playerRepository.findById(tp.getPlayerId())
                    .orElse(null);
            if (player == null) continue;

            TeamPlayerView view = new TeamPlayerView();
            view.teamId = tp.getTeamId();
            view.playerId = tp.getPlayerId();
            view.playerName = player.getName();
            view.skillLevel = player.getSkillLevel();
            view.assignedPrice = tp.getAssignedPrice();
            result.add(view);
        }
        return result;
    }

    private void requireAdmin(Role actingRole) {
        if (actingRole != Role.ADMIN) {
            throw new SecurityException("Only ADMIN can perform this action.");
        }
    }

    public static class TeamPlayerView {
        public Long teamId;
        public Long playerId;
        public String playerName;
        public String skillLevel;
        public int assignedPrice;
    }
}

