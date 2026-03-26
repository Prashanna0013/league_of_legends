package com.example.lol.controller;

import com.example.lol.entity.Match;
import com.example.lol.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/match")

public class MatchController {
    @Autowired
    private MatchService matchService;

    @PostMapping("/add")
    public Match addMatch(@RequestBody Match match){
        return matchService.saveMatch(match);
    }

    @GetMapping("/get")
    public List<Match> getAllMatches(){
        return matchService.getAllMatches();
    }

    @GetMapping("/get/{id}")
    public Optional<Match> getMatchById(@PathVariable Long id){
        return matchService.getMatchById(id);
    }

    @PutMapping("/update/{id}")
    public Match updateMatch(@PathVariable Long id, @RequestBody Match matchDetails){
        return matchService.updateMatch(id, matchDetails);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMatch(@PathVariable Long id){
        matchService.deleteMatch(id);
        return "Match deleted successfully.";
    }
}

