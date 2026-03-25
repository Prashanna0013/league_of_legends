package com.example.lol.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leaderboards")
@Data
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamId;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int points;

    public Leaderboard() {
    }

    public Leaderboard(Long teamId, int matchesPlayed, int wins, int losses, int points) {
        this.teamId = teamId;
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.points = points;
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

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

