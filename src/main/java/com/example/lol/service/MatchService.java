package com.example.lol.service;

import com.example.lol.entity.Match;
import com.example.lol.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public Match saveMatch(Match match){
        return matchRepository.save(match);
    }

    public List<Match> getAllMatches(){
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(Long matchId){
        return matchRepository.findById(matchId);
    }

    public void deleteMatch(Long matchId){
        matchRepository.deleteById(matchId);
    }

    public Match updateMatch(Long matchId, Match matchDetails){
        Optional<Match> match = matchRepository.findById(matchId);
        if(match.isPresent()){
            Match existingMatch = match.get();
            if(matchDetails.getTeamA() != null) existingMatch.setTeamA(matchDetails.getTeamA());
            if(matchDetails.getTeamB() != null) existingMatch.setTeamB(matchDetails.getTeamB());
            if(matchDetails.getVenue() != null) existingMatch.setVenue(matchDetails.getVenue());
            if(matchDetails.getMatchDate() != null) existingMatch.setMatchDate(matchDetails.getMatchDate());
            if(matchDetails.getStatus() != null) existingMatch.setStatus(matchDetails.getStatus());
            return matchRepository.save(existingMatch);
        }
        return null;
    }
}

