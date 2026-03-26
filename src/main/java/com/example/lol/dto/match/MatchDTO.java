package com.example.lol.dto.match;

import java.time.LocalDateTime;

public class MatchDTO {
    public Long id;
    public Long teamAId;
    public Long teamBId;
    public Long gameId;
    public String venue;
    public LocalDateTime dateTime;
    public String status;
    public int teamATotal;
    public int teamBTotal;
    public Long winnerTeamId;
}

