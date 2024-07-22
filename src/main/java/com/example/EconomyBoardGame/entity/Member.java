package com.example.EconomyBoardGame.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private int gold = 0;

    @Column(nullable = false)
    private int manpowerLow = 0;

    @Column(nullable = false)
    private int manpowerMedium = 0;

    @Column(nullable = false)
    private int manpowerHigh = 0;

    @Column(nullable = false)
    private int minepower = 0;

    // Getters and setters
}