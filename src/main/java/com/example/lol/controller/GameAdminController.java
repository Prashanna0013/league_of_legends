package com.example.lol.controller;

import com.example.lol.dto.game.CreateGameRequest;
import com.example.lol.dto.game.ScoreRuleResponse;
import com.example.lol.dto.game.ScoreRuleRequest;
import com.example.lol.entity.Game;
import com.example.lol.entity.Role;
import com.example.lol.entity.ScoreRule;
import com.example.lol.security.CurrentUserService;
import com.example.lol.service.GameAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameAdminController {
    private final GameAdminService gameAdminService;
    private final CurrentUserService currentUserService;

    public GameAdminController(GameAdminService gameAdminService, CurrentUserService currentUserService) {
        this.gameAdminService = gameAdminService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/games")
    public Game createGame(@RequestBody CreateGameRequest request) {
        Role role = currentUserService.getCurrentRole();
        return gameAdminService.createGame(role, request.name);
    }

    @GetMapping("/games")
    public List<Game> getAllGames() {
        // Intentionally available to any authenticated role; creating/updating remains ADMIN-only.
        currentUserService.getCurrentRole();
        return gameAdminService.getAllGames();
    }

    @GetMapping("/games/{id}/rules")
    public List<ScoreRuleResponse> getRules(@PathVariable("id") Long gameId) {
        List<ScoreRule> rules = gameAdminService.getRulesByGameId(gameId);
        return rules.stream().map(r -> {
            ScoreRuleResponse resp = new ScoreRuleResponse();
            resp.id = r.getId();
            resp.gameId = r.getGameId();
            resp.ruleName = r.getRuleName();
            resp.points = r.getPoints();
            return resp;
        }).toList();
    }
}

