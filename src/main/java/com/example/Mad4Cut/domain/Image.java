package com.example.Mad4Cut.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="imageId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;


    @Column(name = "url")
    private String url;

    @Builder
    public Image(String url, Member member) {
        this.url = url;
        this.member = member;
    }

}
