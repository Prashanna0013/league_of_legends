package com.example.lol.service;

import com.example.lol.entity.Game;
import com.example.lol.entity.Match;
import com.example.lol.entity.Role;
import com.example.lol.entity.Team;
import com.example.lol.repository.GameRepository;
import com.example.lol.repository.MatchRepository;
import com.example.lol.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SmartMatchSchedulingService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    public SmartMatchSchedulingService(MatchRepository matchRepository, TeamRepository teamRepository, GameRepository gameRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    public ScheduleMatchResponse scheduleMatch(
            Long actingUserId,
            Role actingRole,
            Long teamAId,
            Long teamBId,
            Long gameId,
            String venue,
            LocalDateTime dateTime,
            boolean suggestNextSlot
    ) {
        if (actingRole != Role.ADMIN) {
            throw new SecurityException("Only ADMIN can schedule matches.");
        }
        if (Objects.equals(teamAId, teamBId)) {
            throw new IllegalArgumentException("teamAId and teamBId must be different");
        }
        if (venue == null || venue.isBlank()) {
            throw new IllegalArgumentException("venue is required");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime is required");
        }

        Team teamA = teamRepository.findById(teamAId).orElseThrow(() -> new IllegalArgumentException("TeamA not found"));
        Team teamB = teamRepository.findById(teamBId).orElseThrow(() -> new IllegalArgumentException("TeamB not found"));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));

        // Try requested slot first
        if (!hasConflict(teamAId, teamBId, venue, dateTime)) {
            Match created = createMatch(teamAId, teamBId, game.getId(), venue, dateTime);
            return new ScheduleMatchResponse(created, null);
        }

        if (!suggestNextSlot) {
            throw new IllegalStateException("Scheduling conflict detected for requested slot");
        }

        // Suggest next available slot (simple step search)
        int incrementMinutes = 30;
        int maxAttempts = 20;
        for (int i = 1; i <= maxAttempts; i++) {
            LocalDateTime candidate = dateTime.plusMinutes((long) i * incrementMinutes);
            if (!hasConflict(teamAId, teamBId, venue, candidate)) {
                Match created = createMatch(teamAId, teamBId, game.getId(), venue, candidate);
                return new ScheduleMatchResponse(created, candidate);
            }
        }

        throw new IllegalStateException("No available slot found within search window");
    }

    private boolean hasConflict(Long teamAId, Long teamBId, String venue, LocalDateTime dateTime) {
        List<Match> matchesAtTime = matchRepository.findByMatchDate(dateTime);
        for (Match m : matchesAtTime) {
            // Team conflict: same team cannot play two matches at same time
            boolean teamConflict = Objects.equals(m.getTeamA(), teamAId)
                    || Objects.equals(m.getTeamB(), teamAId)
                    || Objects.equals(m.getTeamA(), teamBId)
                    || Objects.equals(m.getTeamB(), teamBId);

            boolean venueConflict = m.getVenue() != null && m.getVenue().equalsIgnoreCase(venue);

            if (teamConflict) return true;
            if (venueConflict) return true;
        }
        return false;
    }

    private Match createMatch(Long teamAId, Long teamBId, Long gameId, String venue, LocalDateTime dateTime) {
        Match match = new Match();
        match.setTeamA(teamAId);
        match.setTeamB(teamBId);
        match.setGameId(gameId);
        match.setVenue(venue);
        match.setMatchDate(dateTime);
        match.setStatus("Scheduled");
        match.setTeamATotal(0);
        match.setTeamBTotal(0);
        match.setWinnerTeamId(null);
        return matchRepository.save(match);
    }

    public static class ScheduleMatchResponse {
        private final Match match;
        private final LocalDateTime scheduledDateTimeSuggestion;

        public ScheduleMatchResponse(Match match, LocalDateTime scheduledDateTimeSuggestion) {
            this.match = match;
            this.scheduledDateTimeSuggestion = scheduledDateTimeSuggestion;
        }

        public Match getMatch() {
            return match;
        }

        public LocalDateTime getScheduledDateTimeSuggestion() {
            return scheduledDateTimeSuggestion;
        }
    }
}

