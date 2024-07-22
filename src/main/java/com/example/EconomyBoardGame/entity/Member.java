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

    @Column
    private int gold;

    @Column(nullable = false)
    private int manpowerLow;

    @Column(nullable = false)
    private int manpowerMedium;

    @Column(nullable = false)
    private int manpowerHigh;

    @Column(nullable = false)
    private int minepower;
}