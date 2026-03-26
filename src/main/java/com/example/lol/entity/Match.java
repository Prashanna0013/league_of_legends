package com.example.lol.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamA;
    private Long teamB;

    @Column(name = "game_id")
    private Long gameId;
    private String venue;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime matchDate;
    private String status; // Scheduled / Completed

    @Column(name = "team_a_total", nullable = false)
    private int teamATotal = 0;

    @Column(name = "team_b_total", nullable = false)
    private int teamBTotal = 0;

    @Column(name = "winner_team_id")
    private Long winnerTeamId;

    public Match() {
    }

    public Match(Long teamA, Long teamB, String venue, LocalDateTime matchDate, String status) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.venue = venue;
        this.matchDate = matchDate;
        this.status = status;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getTeamATotal() {
        return teamATotal;
    }

    public void setTeamATotal(int teamATotal) {
        this.teamATotal = teamATotal;
    }

    public int getTeamBTotal() {
        return teamBTotal;
    }

    public void setTeamBTotal(int teamBTotal) {
        this.teamBTotal = teamBTotal;
    }

    public Long getWinnerTeamId() {
        return winnerTeamId;
    }

    public void setWinnerTeamId(Long winnerTeamId) {
        this.winnerTeamId = winnerTeamId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamA() {
        return teamA;
    }

    public void setTeamA(Long teamA) {
        this.teamA = teamA;
    }

    public Long getTeamB() {
        return teamB;
    }

    public void setTeamB(Long teamB) {
        this.teamB = teamB;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

