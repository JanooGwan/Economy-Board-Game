package com.example.EconomyBoardGame.entity;

import com.example.EconomyBoardGame.repository.MemberRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Size(max = 32, message = "닉네임은 최대 32자까지 가능합니다.")
    private String nickname;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 200)
    @Size(max = 200, message = "자기소개는 최대 200자까지 가능합니다.")
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

    @Column(nullable = false)
    private int clickCount;

    @OneToMany(mappedBy = "writer")
    private List<Post> posts;

}