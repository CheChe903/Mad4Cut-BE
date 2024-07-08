package com.example.Mad4Cut.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="stickerId")
    private Long id;

    @Column(name ="url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name="shared")
    private boolean shared;


    @Builder
    public Sticker(String url, Member member, Boolean shared) {
        this.url = url;
        this.member = member;
        this.shared = shared;
    }
}
