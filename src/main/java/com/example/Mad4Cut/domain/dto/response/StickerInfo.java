package com.example.Mad4Cut.domain.dto.response;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StickerInfo {

    private Long id;
    private String url;
}