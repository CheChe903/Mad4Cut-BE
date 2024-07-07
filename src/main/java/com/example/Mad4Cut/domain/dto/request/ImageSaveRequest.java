package com.example.Mad4Cut.domain.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ImageSaveRequest {

    private MultipartFile file;
}
