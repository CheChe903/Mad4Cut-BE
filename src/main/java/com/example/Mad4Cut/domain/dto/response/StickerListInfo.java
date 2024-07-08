package com.example.Mad4Cut.domain.dto.response;


import com.example.Mad4Cut.domain.Sticker;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StickerListInfo {

    private List<Sticker> stickers;
}
