package com.example.lol.repository;

import com.example.lol.entity.TeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {

    boolean existsByTeamIdAndPlayerId(Long teamId, Long playerId);

    boolean existsByPlayerId(Long playerId);

    java.util.Optional<TeamPlayer> findByPlayerIdAndTeamId(Long playerId, Long teamId);

    java.util.List<TeamPlayer> findByTeamId(Long teamId);

    java.util.Optional<TeamPlayer> findByPlayerId(Long playerId);
}
