package com.example.Mad4Cut.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="memberId")
    private Long id;

    @Column(name ="userId")
    private String userId;


    @Column(name = "nickname")
    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Image> images;


    @Builder
    public Member(String userId, String email, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}

