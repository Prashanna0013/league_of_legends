package com.example.lol.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "player_badges",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "badge_id"})
)
public class PlayerBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "badge_id", nullable = false)
    private Long badgeId;

    public PlayerBadge() {}

    public PlayerBadge(Long playerId, Long badgeId) {
        this.playerId = playerId;
        this.badgeId = badgeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }
}

