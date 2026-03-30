package com.example.lol.service;

import com.example.lol.entity.Leaderboard;
import com.example.lol.entity.Match;
import com.example.lol.repository.LeaderboardRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LeagueLeaderboardService {
    private static final int WIN_POINTS = 3;
    private static final int DRAW_POINTS = 1;

    private final LeaderboardRepository leaderboardRepository;

    public LeagueLeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public void applyMatchResult(Match match) {
        if (match == null) throw new IllegalArgumentException("match is required");
        Long teamA = match.getTeamA();
        Long teamB = match.getTeamB();
        Long winnerTeamId = match.getWinnerTeamId(); // null => draw/unknown

        Leaderboard lbA = findOrCreate(teamA);
        Leaderboard lbB = findOrCreate(teamB);

        lbA.setMatchesPlayed(lbA.getMatchesPlayed() + 1);
        lbB.setMatchesPlayed(lbB.getMatchesPlayed() + 1);

        if (winnerTeamId == null) {
            // Draw: keep wins/losses unchanged, award points
            lbA.setPoints(lbA.getPoints() + DRAW_POINTS);
            lbB.setPoints(lbB.getPoints() + DRAW_POINTS);
        } else if (winnerTeamId.equals(teamA)) {
            lbA.setWins(lbA.getWins() + 1);
            lbB.setLosses(lbB.getLosses() + 1);
            lbA.setPoints(lbA.getPoints() + WIN_POINTS);
        } else if (winnerTeamId.equals(teamB)) {
            lbB.setWins(lbB.getWins() + 1);
            lbA.setLosses(lbA.getLosses() + 1);
            lbB.setPoints(lbB.getPoints() + WIN_POINTS);
        } else {
            throw new IllegalStateException("Match winnerTeamId does not match teamA/teamB");
        }

        leaderboardRepository.save(lbA);
        leaderboardRepository.save(lbB);
    }

    public List<Leaderboard> getRankedLeaderboard() {
        List<Leaderboard> all = leaderboardRepository.findAll();
        all.sort(Comparator
                .comparingInt(Leaderboard::getPoints).reversed()
                .thenComparingInt(Leaderboard::getWins).reversed()
                .thenComparingInt(Leaderboard::getMatchesPlayed).reversed());
        return all;
    }

    private Leaderboard findOrCreate(Long teamId) {
        return leaderboardRepository.findByTeamId(teamId)
                .orElseGet(() -> {
                    Leaderboard lb = new Leaderboard();
                    lb.setTeamId(teamId);
                    lb.setMatchesPlayed(0);
                    lb.setWins(0);
                    lb.setLosses(0);
                    lb.setPoints(0);
                    return lb;
                });
    }
}

