package com.example.lol.dto.match;

import java.util.List;

public class UpdateMatchScoreRequest {
    public Long matchId;
    public List<MatchScoreItemDTO> scores;
}

