package com.example.Mad4Cut.domain.dto.response;

import com.example.Mad4Cut.domain.Image;
import lombok.*;

import java.util.List;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImageListInfo {

    private List<Image> images;
}
