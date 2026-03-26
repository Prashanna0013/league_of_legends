package com.example.lol.repository;

import com.example.lol.entity.PlayerBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerBadgeRepository extends JpaRepository<PlayerBadge, Long> {
    boolean existsByPlayerIdAndBadgeId(Long playerId, Long badgeId);

    List<PlayerBadge> findByPlayerId(Long playerId);

    Optional<PlayerBadge> findByPlayerIdAndBadgeId(Long playerId, Long badgeId);
}

