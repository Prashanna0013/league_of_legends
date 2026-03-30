package com.example.lol.controller;

import com.example.lol.dto.match.CreateMatchRequest;
import com.example.lol.dto.match.MatchDTO;
import com.example.lol.dto.match.ScheduleMatchResponseDTO;
import com.example.lol.entity.Match;
import com.example.lol.entity.Role;
import com.example.lol.security.CurrentUserService;
import com.example.lol.service.MatchService;
import com.example.lol.service.SmartMatchSchedulingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchesController {
    private final SmartMatchSchedulingService schedulingService;
    private final CurrentUserService currentUserService;
    private final MatchService matchService;

    public MatchesController(SmartMatchSchedulingService schedulingService, CurrentUserService currentUserService, MatchService matchService) {
        this.schedulingService = schedulingService;
        this.currentUserService = currentUserService;
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public ResponseEntity<?> schedule(@RequestBody CreateMatchRequest request) {
        Role role = currentUserService.getCurrentRole();
        Long userId = currentUserService.getCurrentUserId();

        boolean suggest = request.suggestNextSlot != null ? request.suggestNextSlot : true;

        var resp = schedulingService.scheduleMatch(
                userId,
                role,
                request.teamAId,
                request.teamBId,
                request.gameId,
                request.venue,
                request.dateTime,
                suggest
        );

        ScheduleMatchResponseDTO out = new ScheduleMatchResponseDTO();
        out.match = toMatchDTO(resp.getMatch());
        out.suggestedDateTime = resp.getScheduledDateTimeSuggestion();
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @GetMapping("/matches")
    public List<MatchDTO> getAll() {
        return matchService.getAllMatches()
                .stream()
                .map(this::toMatchDTO)
                .toList();
    }

    private MatchDTO toMatchDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.id = match.getId();
        dto.teamAId = match.getTeamA();
        dto.teamBId = match.getTeamB();
        dto.gameId = match.getGameId();
        dto.venue = match.getVenue();
        dto.dateTime = match.getMatchDate();
        dto.status = match.getStatus();
        dto.teamATotal = match.getTeamATotal();
        dto.teamBTotal = match.getTeamBTotal();
        dto.winnerTeamId = match.getWinnerTeamId();
        return dto;
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleForbidden(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}

