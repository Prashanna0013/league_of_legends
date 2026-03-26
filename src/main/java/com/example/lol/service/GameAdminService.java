package com.example.lol.service;

import com.example.lol.entity.Game;
import com.example.lol.entity.Role;
import com.example.lol.entity.ScoreRule;
import com.example.lol.repository.GameRepository;
import com.example.lol.repository.ScoreRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameAdminService {
    private final GameRepository gameRepository;
    private final ScoreRuleRepository scoreRuleRepository;

    public GameAdminService(GameRepository gameRepository, ScoreRuleRepository scoreRuleRepository) {
        this.gameRepository = gameRepository;
        this.scoreRuleRepository = scoreRuleRepository;
    }

    public Game createGame(Role actingRole, String name) {
        requireAdmin(actingRole);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Game name is required");
        }
        // unique constraint on game name will enforce duplicates
        return gameRepository.save(new Game(name.trim()));
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public ScoreRule createScoreRule(Role actingRole, Long gameId, String ruleName, int points) {
        requireAdmin(actingRole);

        if (gameId == null) throw new IllegalArgumentException("gameId is required");
        if (ruleName == null || ruleName.isBlank()) throw new IllegalArgumentException("ruleName is required");
        if (points <= 0) throw new IllegalArgumentException("points must be > 0");

        // Ensure game exists
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));

        ScoreRule rule = new ScoreRule(game.getId(), ruleName.trim(), points);
        return scoreRuleRepository.save(rule);
    }

    public List<ScoreRule> getRulesByGameId(Long gameId) {
        return scoreRuleRepository.findByGameId(gameId);
    }

    public ScoreRule updateScoreRule(Role actingRole, Long ruleId, String ruleName, Integer points) {
        requireAdmin(actingRole);

        ScoreRule existing = scoreRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("ScoreRule not found: " + ruleId));

        if (ruleName != null && !ruleName.isBlank()) {
            existing.setRuleName(ruleName.trim());
        }
        if (points != null) {
            if (points <= 0) throw new IllegalArgumentException("points must be > 0");
            existing.setPoints(points);
        }

        return scoreRuleRepository.save(existing);
    }

    private void requireAdmin(Role actingRole) {
        if (actingRole != Role.ADMIN) {
            throw new SecurityException("Only ADMIN can manage games/score rules.");
        }
    }
}

