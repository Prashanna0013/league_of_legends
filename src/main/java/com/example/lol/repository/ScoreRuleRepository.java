package com.example.lol.repository;

import com.example.lol.entity.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRuleRepository extends JpaRepository<ScoreRule, Long> {
    List<ScoreRule> findByGameId(Long gameId);
}

