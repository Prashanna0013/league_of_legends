package com.example.lol.controller;

import com.example.lol.dto.leaderboard.LeaderboardEntryDTO;
import com.example.lol.entity.Leaderboard;
import com.example.lol.security.CurrentUserService;
import com.example.lol.service.LeagueLeaderboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardControllerV2 {
    private final LeagueLeaderboardService leagueLeaderboardService;

    public LeaderboardControllerV2(LeagueLeaderboardService leagueLeaderboardService) {
        this.leagueLeaderboardService = leagueLeaderboardService;
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntryDTO> getLeaderboard() {
        List<Leaderboard> ranked = leagueLeaderboardService.getRankedLeaderboard();
        return ranked.stream().map(lb -> {
            LeaderboardEntryDTO dto = new LeaderboardEntryDTO();
            dto.teamId = lb.getTeamId();
            dto.matchesPlayed = lb.getMatchesPlayed();
            dto.wins = lb.getWins();
            dto.losses = lb.getLosses();
            dto.points = lb.getPoints();
            return dto;
        }).toList();
    }
}

