package com.example.lol.controller;

import com.example.lol.dto.match.MatchScoreItemDTO;
import com.example.lol.dto.match.MatchScoreUpdateResponseDTO;
import com.example.lol.dto.match.UpdateMatchScoreRequest;
import com.example.lol.entity.Match;
import com.example.lol.entity.Role;
import com.example.lol.security.CurrentUserService;
import com.example.lol.service.AdminMatchScoringService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ScoringController {
    private final AdminMatchScoringService adminMatchScoringService;
    private final CurrentUserService currentUserService;

    public ScoringController(AdminMatchScoringService adminMatchScoringService, CurrentUserService currentUserService) {
        this.adminMatchScoringService = adminMatchScoringService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/match-score")
    public ResponseEntity<MatchScoreUpdateResponseDTO> update(@RequestBody UpdateMatchScoreRequest request) {
        Role role = currentUserService.getCurrentRole();
        Long userId = currentUserService.getCurrentUserId();

        List<AdminMatchScoringService.MatchScoreItem> items = new ArrayList<>();
        if (request.scores != null) {
            for (MatchScoreItemDTO s : request.scores) {
                AdminMatchScoringService.MatchScoreItem item = new AdminMatchScoringService.MatchScoreItem();
                item.playerId = s.playerId;
                item.ruleId = s.ruleId;
                item.value = s.value;
                items.add(item);
            }
        }

        var result = adminMatchScoringService.updateMatchScores(userId, role, request.matchId, items);
        MatchScoreUpdateResponseDTO out = new MatchScoreUpdateResponseDTO();
        out.matchId = result.getMatch().getId();
        out.teamATotal = result.getTeamATotal();
        out.teamBTotal = result.getTeamBTotal();
        out.winnerTeamId = result.getWinnerTeamId();
        return ResponseEntity.status(HttpStatus.OK).body(out);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleForbidden(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

