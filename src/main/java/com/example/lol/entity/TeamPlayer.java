package com.example.lol.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamId;
    private Long playerId;

    public TeamPlayer() {
    }

    public TeamPlayer(Long id, Long teamId, Long playerId) {
        this.id = id;
        this.teamId = teamId;
        this.playerId = playerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}