package com.example.lol.controller;

import com.example.lol.entity.Leaderboard;
import com.example.lol.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {
    @Autowired
    private LeaderboardService leaderboardService;

    @PostMapping("/add")
    public Leaderboard addLeaderboard(@RequestBody Leaderboard leaderboard){
        return leaderboardService.saveLeaderboard(leaderboard);
    }

    @GetMapping("/get")
    public List<Leaderboard> getAllLeaderboards(){
        return leaderboardService.getAllLeaderboards();
    }

    @GetMapping("/get/{id}")
    public Optional<Leaderboard> getLeaderboardById(@PathVariable Long id){
        return leaderboardService.getLeaderboardById(id);
    }

    @GetMapping("/get/team/{teamId}")
    public Optional<Leaderboard> getLeaderboardByTeamId(@PathVariable Long teamId){
        return leaderboardService.getLeaderboardByTeamId(teamId);
    }

    @PutMapping("/update/{id}")
    public Leaderboard updateLeaderboard(@PathVariable Long id, @RequestBody Leaderboard leaderboardDetails){
        return leaderboardService.updateLeaderboard(id, leaderboardDetails);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLeaderboard(@PathVariable Long id){
        leaderboardService.deleteLeaderboard(id);
        return "Leaderboard entry deleted successfully.";
    }
}

