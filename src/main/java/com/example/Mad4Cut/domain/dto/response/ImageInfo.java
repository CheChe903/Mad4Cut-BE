package com.example.Mad4Cut.domain.dto.response;


import com.example.Mad4Cut.domain.Member;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImageInfo {

    private Long id;


    private String url;
}
