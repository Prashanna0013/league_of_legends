package com.example.lol.repository;

import com.example.lol.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    java.util.List<Match> findByMatchDate(java.time.LocalDateTime matchDate);

    boolean existsByMatchDateAndVenue(java.time.LocalDateTime matchDate, String venue);
}

