package com.example.lol.repository;

import com.example.lol.entity.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

    java.util.Optional<Leaderboard> findByTeamId(Long teamId);
}

