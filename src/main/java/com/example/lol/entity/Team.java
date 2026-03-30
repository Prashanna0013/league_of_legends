package com.example.lol.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String owner;

    // New fields required by auctions + RBAC team ownership
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(nullable = false)
    private int budget;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false;

    public Team() {
    }

    public Team(Long id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.budget = 0;
        this.isLocked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
