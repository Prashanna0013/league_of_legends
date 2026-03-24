package com.example.lol.service;

import com.example.lol.entity.Player;
import com.example.lol.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player savePlayer(Player player){
        return playerRepository.save(player);
    }
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }
}
