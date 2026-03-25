package com.example.lol.controller;

import com.example.lol.entity.Player;
import com.example.lol.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/add")
    public Player addPlayer(@RequestBody Player player){
        return playerService.savePlayer(player);
    }

    @GetMapping("/get")
    public List<Player> getAllPlayers(){
        return playerService.getAllPlayers();
    }

}
