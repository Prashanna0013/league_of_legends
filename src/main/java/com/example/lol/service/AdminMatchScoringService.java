package com.example.lol.service;

import com.example.lol.entity.Match;
import com.example.lol.entity.MatchScore;
import com.example.lol.entity.PlayerStats;
import com.example.lol.entity.Role;
import com.example.lol.entity.ScoreRule;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.repository.MatchRepository;
import com.example.lol.repository.MatchScoreRepository;
import com.example.lol.repository.ScoreRuleRepository;
import com.example.lol.repository.TeamPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminMatchScoringService {
    private final MatchRepository matchRepository;
    private final MatchScoreRepository matchScoreRepository;
    private final ScoreRuleRepository scoreRuleRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    private final LeagueLeaderboardService leagueLeaderboardService;
    private final AnalyticsService analyticsService;
    private final GamificationService gamificationService;

    public AdminMatchScoringService(
            MatchRepository matchRepository,
            MatchScoreRepository matchScoreRepository,
            ScoreRuleRepository scoreRuleRepository,
            TeamPlayerRepository teamPlayerRepository,
            LeagueLeaderboardService leagueLeaderboardService,
            AnalyticsService analyticsService,
            GamificationService gamificationService
    ) {
        this.matchRepository = matchRepository;
        this.matchScoreRepository = matchScoreRepository;
        this.scoreRuleRepository = scoreRuleRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.leagueLeaderboardService = leagueLeaderboardService;
        this.analyticsService = analyticsService;
        this.gamificationService = gamificationService;
    }

    public MatchScoreUpdateResult updateMatchScores(
            Long actingUserId,
            Role actingRole,
            Long matchId,
            List<MatchScoreItem> scoreItems
    ) {
        if (actingRole != Role.ADMIN) {
            throw new SecurityException("Only ADMIN can modify match scores.");
        }
        if (matchId == null) throw new IllegalArgumentException("matchId is required");
        if (scoreItems == null || scoreItems.isEmpty()) {
            throw new IllegalArgumentException("scores must be provided");
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if ("Completed".equalsIgnoreCase(match.getStatus())) {
            throw new IllegalStateException("Match is already completed. Scoring updates are not allowed in this scaffold.");
        }

        if (match.getGameId() == null) {
            throw new IllegalStateException("Match.gameId is required for dynamic scoring");
        }

        // Load scoring rules for the match game: no hardcoded scoring logic.
        List<ScoreRule> rules = scoreRuleRepository.findByGameId(match.getGameId());
        Map<Long, ScoreRule> ruleById = new HashMap<>();
        for (ScoreRule r : rules) {
            ruleById.put(r.getId(), r);
        }

        Map<Long, Integer> teamTotalsByTeamId = new HashMap<>();
        teamTotalsByTeamId.put(match.getTeamA(), 0);
        teamTotalsByTeamId.put(match.getTeamB(), 0);

        Map<Long, Integer> pointsInMatchByPlayerId = new HashMap<>();

        List<MatchScore> toSave = new ArrayList<>();

        for (MatchScoreItem item : scoreItems) {
            if (item == null) continue;
            if (item.playerId == null || item.ruleId == null) {
                throw new IllegalArgumentException("playerId and ruleId are required for each score item");
            }
            if (item.value < 0) {
                throw new IllegalArgumentException("value cannot be negative");
            }

            ScoreRule rule = ruleById.get(item.ruleId);
            if (rule == null) {
                throw new IllegalArgumentException("ruleId " + item.ruleId + " does not belong to match.gameId " + match.getGameId());
            }

            TeamPlayer tp = teamPlayerRepository.findByPlayerId(item.playerId)
                    .orElseThrow(() -> new IllegalArgumentException("Player " + item.playerId + " is not assigned to any team (auction required)"));

            Long playerTeamId = tp.getTeamId();
            if (!playerTeamId.equals(match.getTeamA()) && !playerTeamId.equals(match.getTeamB())) {
                throw new IllegalArgumentException("Player " + item.playerId + " is not part of match teams");
            }

            int computedPoints = rule.getPoints() * item.value;

            teamTotalsByTeamId.put(playerTeamId, teamTotalsByTeamId.get(playerTeamId) + computedPoints);
            pointsInMatchByPlayerId.put(item.playerId,
                    pointsInMatchByPlayerId.getOrDefault(item.playerId, 0) + computedPoints);

            toSave.add(new MatchScore(match.getId(), item.playerId, item.ruleId, item.value));
        }

        // Replace match scores for this match
        matchScoreRepository.deleteByMatchId(matchId);
        matchScoreRepository.saveAll(toSave);

        int teamATotal = teamTotalsByTeamId.get(match.getTeamA());
        int teamBTotal = teamTotalsByTeamId.get(match.getTeamB());

        match.setTeamATotal(teamATotal);
        match.setTeamBTotal(teamBTotal);

        Long winnerTeamId;
        if (teamATotal > teamBTotal) winnerTeamId = match.getTeamA();
        else if (teamBTotal > teamATotal) winnerTeamId = match.getTeamB();
        else winnerTeamId = null;

        match.setWinnerTeamId(winnerTeamId);
        match.setStatus("Completed");

        matchRepository.save(match);

        // Propagate results
        leagueLeaderboardService.applyMatchResult(match);
        analyticsService.applyPlayerStatsForMatch(winnerTeamId, pointsInMatchByPlayerId);
        gamificationService.applyBadgesAfterMatch(match.getId(), pointsInMatchByPlayerId);

        return new MatchScoreUpdateResult(match, teamATotal, teamBTotal, winnerTeamId);
    }

    public static class MatchScoreItem {
        public Long playerId;
        public Long ruleId;
        public int value;

        public MatchScoreItem() {}

        public MatchScoreItem(Long playerId, Long ruleId, int value) {
            this.playerId = playerId;
            this.ruleId = ruleId;
            this.value = value;
        }
    }

    public static class MatchScoreUpdateResult {
        private final Match match;
        private final int teamATotal;
        private final int teamBTotal;
        private final Long winnerTeamId;

        public MatchScoreUpdateResult(Match match, int teamATotal, int teamBTotal, Long winnerTeamId) {
            this.match = match;
            this.teamATotal = teamATotal;
            this.teamBTotal = teamBTotal;
            this.winnerTeamId = winnerTeamId;
        }

        public Match getMatch() {
            return match;
        }

        public int getTeamATotal() {
            return teamATotal;
        }

        public int getTeamBTotal() {
            return teamBTotal;
        }

        public Long getWinnerTeamId() {
            return winnerTeamId;
        }
    }
}

