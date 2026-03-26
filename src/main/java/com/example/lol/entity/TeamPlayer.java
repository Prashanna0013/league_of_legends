package com.example.lol.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        name = "team_players",
        uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "player_id"})
)
public class TeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "assigned_price", nullable = false)
    private int assignedPrice;

    public TeamPlayer() {
    }

    public TeamPlayer(Long id, Long teamId, Long playerId, int assignedPrice) {
        this.id = id;
        this.teamId = teamId;
        this.playerId = playerId;
        this.assignedPrice = assignedPrice;
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

    public int getAssignedPrice() {
        return assignedPrice;
    }

    public void setAssignedPrice(int assignedPrice) {
        this.assignedPrice = assignedPrice;
    }
}