package com.example.Mad4Cut.domain.dto.response;


import com.example.Mad4Cut.domain.Sticker;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SharedStickerInfo {

    private Sticker sticker;
}
