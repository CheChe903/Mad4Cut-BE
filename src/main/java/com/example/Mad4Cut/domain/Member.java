package com.example.Mad4Cut.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="memberId")
    private Long id;

    @Column(name ="userId")
    private String userId;


    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Builder
    public Member(String userId, String email, String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }
}

