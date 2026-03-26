package com.example.lol.controller;

import com.example.lol.dto.game.ScoreRuleRequest;
import com.example.lol.dto.game.ScoreRuleResponse;
import com.example.lol.entity.Role;
import com.example.lol.entity.ScoreRule;
import com.example.lol.security.CurrentUserService;
import com.example.lol.service.GameAdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreRuleAdminController {
    private final GameAdminService gameAdminService;
    private final CurrentUserService currentUserService;

    public ScoreRuleAdminController(GameAdminService gameAdminService, CurrentUserService currentUserService) {
        this.gameAdminService = gameAdminService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/score-rules")
    public ScoreRuleResponse createOrUpdate(@RequestBody ScoreRuleRequest request) {
        Role role = currentUserService.getCurrentRole();

        ScoreRule rule;
        if (request.ruleId != null) {
            rule = gameAdminService.updateScoreRule(role, request.ruleId, request.ruleName, request.points);
        } else {
            rule = gameAdminService.createScoreRule(role, request.gameId, request.ruleName, request.points);
        }

        ScoreRuleResponse resp = new ScoreRuleResponse();
        resp.id = rule.getId();
        resp.gameId = rule.getGameId();
        resp.ruleName = rule.getRuleName();
        resp.points = rule.getPoints();
        return resp;
    }
}

