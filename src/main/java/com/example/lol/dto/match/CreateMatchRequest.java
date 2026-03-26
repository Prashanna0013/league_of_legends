package com.example.lol.dto.match;

import java.time.LocalDateTime;

public class CreateMatchRequest {
    public Long teamAId;
    public Long teamBId;
    public Long gameId;
    public String venue;
    public LocalDateTime dateTime;

    // If true, service will suggest next available slot when conflict detected
    public Boolean suggestNextSlot;
}

