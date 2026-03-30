package com.example.lol.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "score_rules", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"game_id", "rule_name"})
})
public class ScoreRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(nullable = false)
    private int points;

    public ScoreRule() {}

    public ScoreRule(Long gameId, String ruleName, int points) {
        this.gameId = gameId;
        this.ruleName = ruleName;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

