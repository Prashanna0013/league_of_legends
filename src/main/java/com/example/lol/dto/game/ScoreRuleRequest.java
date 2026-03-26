package com.example.lol.dto.game;

public class ScoreRuleRequest {
    // if ruleId is provided, update is performed
    public Long ruleId;

    public Long gameId;
    public String ruleName;
    public int points;
}

