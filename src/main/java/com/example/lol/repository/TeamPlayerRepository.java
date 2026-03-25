package com.example.lol.repository;

import com.example.lol.entity.TeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {
}
