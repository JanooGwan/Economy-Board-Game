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

    @Column(nullable = false, length = 32)
    private String nickname;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(nullable = false, length = 200)
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
}