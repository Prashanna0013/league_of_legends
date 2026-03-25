package com.example.lol.service;

import com.example.lol.entity.Leaderboard;
import com.example.lol.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaderboardService {
    @Autowired
    private LeaderboardRepository leaderboardRepository;

    public Leaderboard saveLeaderboard(Leaderboard leaderboard){
        return leaderboardRepository.save(leaderboard);
    }

    public List<Leaderboard> getAllLeaderboards(){
        return leaderboardRepository.findAll();
    }

    public Optional<Leaderboard> getLeaderboardById(Long leaderboardId){
        return leaderboardRepository.findById(leaderboardId);
    }

    public Optional<Leaderboard> getLeaderboardByTeamId(Long teamId){
        List<Leaderboard> leaderboards = leaderboardRepository.findAll();
        return leaderboards.stream()
                .filter(lb -> lb.getTeamId().equals(teamId))
                .findFirst();
    }

    public void deleteLeaderboard(Long leaderboardId){
        leaderboardRepository.deleteById(leaderboardId);
    }

    public Leaderboard updateLeaderboard(Long leaderboardId, Leaderboard leaderboardDetails){
        Optional<Leaderboard> leaderboard = leaderboardRepository.findById(leaderboardId);
        if(leaderboard.isPresent()){
            Leaderboard existingLeaderboard = leaderboard.get();
            if(leaderboardDetails.getTeamId() != null) existingLeaderboard.setTeamId(leaderboardDetails.getTeamId());
            if(leaderboardDetails.getMatchesPlayed() > 0) existingLeaderboard.setMatchesPlayed(leaderboardDetails.getMatchesPlayed());
            if(leaderboardDetails.getWins() >= 0) existingLeaderboard.setWins(leaderboardDetails.getWins());
            if(leaderboardDetails.getLosses() >= 0) existingLeaderboard.setLosses(leaderboardDetails.getLosses());
            if(leaderboardDetails.getPoints() >= 0) existingLeaderboard.setPoints(leaderboardDetails.getPoints());
            return leaderboardRepository.save(existingLeaderboard);
        }
        return null;
    }
}

