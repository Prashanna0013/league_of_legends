package com.example.lol.service;

import com.example.lol.entity.PlayerStats;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.repository.MatchRepository;
import com.example.lol.repository.MatchScoreRepository;
import com.example.lol.repository.PlayerStatsRepository;
import com.example.lol.repository.TeamPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    private final PlayerStatsRepository playerStatsRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    // Injected for potential "most improved" computation later
    @SuppressWarnings("unused")
    private final MatchRepository matchRepository;
    @SuppressWarnings("unused")
    private final MatchScoreRepository matchScoreRepository;

    public AnalyticsService(
            PlayerStatsRepository playerStatsRepository,
            TeamPlayerRepository teamPlayerRepository,
            MatchRepository matchRepository,
            MatchScoreRepository matchScoreRepository
    ) {
        this.playerStatsRepository = playerStatsRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.matchRepository = matchRepository;
        this.matchScoreRepository = matchScoreRepository;
    }

    public void applyPlayerStatsForMatch(
            Long winnerTeamId,
            Map<Long, Integer> pointsInMatchByPlayerId
    ) {
        if (pointsInMatchByPlayerId == null || pointsInMatchByPlayerId.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, Integer> e : pointsInMatchByPlayerId.entrySet()) {
            Long playerId = e.getKey();
            int points = e.getValue();

            PlayerStats stats = playerStatsRepository.findByPlayerId(playerId)
                    .orElseGet(() -> new PlayerStats(playerId));

            stats.setMatchesPlayed(stats.getMatchesPlayed() + 1);
            stats.setTotalPoints(stats.getTotalPoints() + points);

            if (winnerTeamId != null) {
                TeamPlayer tp = teamPlayerRepository.findByPlayerId(playerId)
                        .orElseThrow(() -> new IllegalStateException("Player " + playerId + " not assigned to any team"));
                if (winnerTeamId.equals(tp.getTeamId())) {
                    stats.setWins(stats.getWins() + 1);
                }
            }

            playerStatsRepository.save(stats);
        }
    }

    // Leaderboard-style "Top Performer": highest total points
    public List<PlayerStats> getTopPerformers(int limit) {
        List<PlayerStats> all = playerStatsRepository.findAll();
        all.sort(Comparator
                .comparingInt(PlayerStats::getTotalPoints).reversed()
                .thenComparingInt(PlayerStats::getWins).reversed());
        return all.subList(0, Math.min(limit, all.size()));
    }

    // MVP-ish approximation: best average points per match
    public PlayerStats getMostImprovedPlayerApprox() {
        List<PlayerStats> all = playerStatsRepository.findAll();
        PlayerStats best = null;
        double bestAvg = Double.NEGATIVE_INFINITY;

        for (PlayerStats ps : all) {
            if (ps.getMatchesPlayed() <= 0) continue;
            double avg = (double) ps.getTotalPoints() / (double) ps.getMatchesPlayed();
            if (avg > bestAvg) {
                bestAvg = avg;
                best = ps;
            }
        }
        return best;
    }

    public List<PlayerStats> getAllPlayerStats() {
        return playerStatsRepository.findAll();
    }
}

