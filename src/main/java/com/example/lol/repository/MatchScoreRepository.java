package com.example.lol.repository;

import com.example.lol.entity.MatchScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchScoreRepository extends JpaRepository<MatchScore, Long> {
    List<MatchScore> findByMatchId(Long matchId);

    void deleteByMatchId(Long matchId);

    List<MatchScore> findByPlayerId(Long playerId);
}

