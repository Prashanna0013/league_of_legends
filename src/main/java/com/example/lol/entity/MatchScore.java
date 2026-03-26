package com.example.lol.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "match_scores",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "player_id", "rule_id"})
)
public class MatchScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(nullable = false)
    private int value;

    public MatchScore() {}

    public MatchScore(Long matchId, Long playerId, Long ruleId, int value) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.ruleId = ruleId;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

