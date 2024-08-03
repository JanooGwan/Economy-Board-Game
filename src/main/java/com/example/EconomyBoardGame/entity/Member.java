package com.example.EconomyBoardGame.entity;

import com.example.EconomyBoardGame.repository.MemberRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

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

    @OneToMany(mappedBy = "writer")
    private List<Post> posts;

}