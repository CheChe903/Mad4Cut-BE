package com.example.Mad4Cut.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Frame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="frameId")
    private Long id;

    @Column(name = "url")
    private String url;

    @Builder
    public Frame(String url) {
        this.url = url;
    }

}
