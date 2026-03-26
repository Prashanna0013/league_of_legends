package com.example.lol.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String department;
    private String sport;
    private String skillLevel;
    private String contact;

    // Needed for auction/bidding
    @Column(name = "base_price", nullable = false)
    private int basePrice;

    public Player() {
    }

    public Player(Long id, String name, String department, String sport, String skillLevel, String contact) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.sport = sport;
        this.skillLevel = skillLevel;
        this.contact = contact;
        this.basePrice = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }
}
