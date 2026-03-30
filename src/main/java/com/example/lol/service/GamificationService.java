package com.example.lol.service;

import com.example.lol.entity.Badge;
import com.example.lol.entity.Match;
import com.example.lol.entity.PlayerBadge;
import com.example.lol.entity.PlayerStats;
import com.example.lol.entity.TeamPlayer;
import com.example.lol.repository.BadgeRepository;
import com.example.lol.repository.MatchRepository;
import com.example.lol.repository.MatchScoreRepository;
import com.example.lol.repository.PlayerBadgeRepository;
import com.example.lol.repository.PlayerStatsRepository;
import com.example.lol.repository.TeamPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GamificationService {
    private final BadgeRepository badgeRepository;
    private final PlayerBadgeRepository playerBadgeRepository;
    private final PlayerStatsRepository playerStatsRepository;

    private final TeamPlayerRepository teamPlayerRepository;
    private final MatchRepository matchRepository;
    private final MatchScoreRepository matchScoreRepository;

    public GamificationService(
            BadgeRepository badgeRepository,
            PlayerBadgeRepository playerBadgeRepository,
            PlayerStatsRepository playerStatsRepository,
            TeamPlayerRepository teamPlayerRepository,
            MatchRepository matchRepository,
            MatchScoreRepository matchScoreRepository
    ) {
        this.badgeRepository = badgeRepository;
        this.playerBadgeRepository = playerBadgeRepository;
        this.playerStatsRepository = playerStatsRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.matchRepository = matchRepository;
        this.matchScoreRepository = matchScoreRepository;
    }

    public void applyBadgesAfterMatch(
            Long matchId,
            Map<Long, Integer> pointsInMatchByPlayerId
    ) {
        // If no badges are configured, just skip.
        List<Badge> badges = badgeRepository.findAll();
        if (badges == null || badges.isEmpty()) {
            return;
        }

        // Precompute match max scorer(s)
        int maxMatchPoints = pointsInMatchByPlayerId == null || pointsInMatchByPlayerId.isEmpty()
                ? 0
                : pointsInMatchByPlayerId.values().stream().max(Integer::compareTo).orElse(0);

        Set<Long> topScorerCandidates = new HashSet<>();
        if (pointsInMatchByPlayerId != null) {
            for (Map.Entry<Long, Integer> e : pointsInMatchByPlayerId.entrySet()) {
                if (e.getValue() == maxMatchPoints) {
                    topScorerCandidates.add(e.getKey());
                }
            }
        }

        for (Badge badge : badges) {
            String badgeName = badge.getName() == null ? "" : badge.getName().trim().toUpperCase(Locale.ROOT);

            if (badgeName.contains("TOP_SCORER")) {
                int threshold = parseThreshold(badge.getCondition());
                for (Long playerId : topScorerCandidates) {
                    PlayerStats stats = playerStatsRepository.findByPlayerId(playerId).orElse(null);
                    if (stats == null) continue;
                    if (stats.getTotalPoints() >= threshold) {
                        assignIfMissing(playerId, badge.getId());
                    }
                }
            } else if (badgeName.contains("WINNING_STREAK")) {
                int threshold = parseThreshold(badge.getCondition());
                // Evaluate only players that scored in the match (keeps it fast)
                if (pointsInMatchByPlayerId != null) {
                    for (Long playerId : pointsInMatchByPlayerId.keySet()) {
                        int streak = calculateWinningStreak(playerId);
                        if (streak >= threshold) {
                            assignIfMissing(playerId, badge.getId());
                        }
                    }
                }
            }
        }
    }

    private void assignIfMissing(Long playerId, Long badgeId) {
        if (playerBadgeRepository.existsByPlayerIdAndBadgeId(playerId, badgeId)) {
            return;
        }
        playerBadgeRepository.save(new PlayerBadge(playerId, badgeId));
    }

    private int parseThreshold(String condition) {
        if (condition == null) return 0;
        // Support strings like "...>=500" or ">= 500"
        Pattern p = Pattern.compile(">=\\s*(\\d+)");
        Matcher m = p.matcher(condition);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    private int calculateWinningStreak(Long playerId) {
        TeamPlayer tp = teamPlayerRepository.findByPlayerId(playerId)
                .orElse(null);
        if (tp == null) {
            return 0;
        }
        Long playerTeamId = tp.getTeamId();

        List<com.example.lol.entity.MatchScore> scores = matchScoreRepository.findByPlayerId(playerId);
        if (scores == null || scores.isEmpty()) {
            return 0;
        }

        // Distinct match IDs where this player has scores
        Set<Long> matchIds = new HashSet<>();
        for (var s : scores) {
            matchIds.add(s.getMatchId());
        }

        List<Match> matches = matchRepository.findAllById(matchIds);
        matches.sort(Comparator.comparing(Match::getMatchDate));

        // Starting from latest match, count consecutive wins
        int streak = 0;
        for (int i = matches.size() - 1; i >= 0; i--) {
            Match m = matches.get(i);
            if (m.getWinnerTeamId() != null && m.getWinnerTeamId().equals(playerTeamId)) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    // Levels are computed on the fly (not persisted to keep this scaffold minimal).
    public String computeLevel(int totalPoints) {
        if (totalPoints < 1000) return "Beginner";
        if (totalPoints < 2000) return "Intermediate";
        return "Pro";
    }
}

