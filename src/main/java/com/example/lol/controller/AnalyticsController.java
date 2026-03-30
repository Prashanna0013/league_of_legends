package com.example.lol.controller;

import com.example.lol.dto.analytics.PlayerStatsDTO;
import com.example.lol.dto.analytics.TopPerformersResponseDTO;
import com.example.lol.entity.Player;
import com.example.lol.entity.PlayerStats;
import com.example.lol.repository.PlayerRepository;
import com.example.lol.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final PlayerRepository playerRepository;

    public AnalyticsController(AnalyticsService analyticsService, PlayerRepository playerRepository) {
        this.analyticsService = analyticsService;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/player-stats")
    public List<PlayerStatsDTO> getPlayerStats() {
        return analyticsService.getAllPlayerStats()
                .stream()
                .map(ps -> toDTO(ps))
                .toList();
    }

    @GetMapping("/top-performers")
    public TopPerformersResponseDTO topPerformers(@RequestParam(name = "limit", required = false) Integer limit) {
        int l = (limit == null || limit <= 0) ? 5 : limit;
        List<PlayerStats> top = analyticsService.getTopPerformers(l);
        PlayerStats mostImproved = analyticsService.getMostImprovedPlayerApprox();

        TopPerformersResponseDTO out = new TopPerformersResponseDTO();
        out.topPerformers = top.stream().map(this::toDTO).toList();
        out.mostImproved = mostImproved == null ? null : toDTO(mostImproved);
        return out;
    }

    private PlayerStatsDTO toDTO(PlayerStats stats) {
        PlayerStatsDTO dto = new PlayerStatsDTO();
        dto.playerId = stats.getPlayerId();
        dto.matchesPlayed = stats.getMatchesPlayed();
        dto.totalPoints = stats.getTotalPoints();
        dto.wins = stats.getWins();

        Player player = playerRepository.findById(stats.getPlayerId()).orElse(null);
        dto.playerName = player == null ? null : player.getName();
        return dto;
    }
}

