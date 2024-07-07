package com.example.Mad4Cut.domain.dto.response;

import com.example.Mad4Cut.domain.Frame;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FrameListInfo {
    private List<Frame> frames;
}
