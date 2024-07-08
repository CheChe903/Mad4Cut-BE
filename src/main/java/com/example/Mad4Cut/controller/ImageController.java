package com.example.Mad4Cut.controller;

import com.example.Mad4Cut.domain.Image;
import com.example.Mad4Cut.domain.dto.response.ImageInfo;
import com.example.Mad4Cut.domain.dto.response.ImageListInfo;
import com.example.Mad4Cut.security.JwtTokenProvider;
import com.example.Mad4Cut.service.ImageService;
import com.example.Mad4Cut.support.ApiResponse;
import com.example.Mad4Cut.support.ApiResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
public class ImageController {

    private final ImageService imageService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ImageController(ImageService imageService, JwtTokenProvider jwtTokenProvider) {
        this.imageService = imageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/gallery/save")
    public ApiResponse<ApiResponse.SuccessBody<ImageInfo>> uploadImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        Long memberId = findMemberByToken(request);
        String fileUrl = imageService.saveImage(file, memberId);

        ImageInfo res = ImageInfo.builder()
                .id(memberId)
                .url(fileUrl)
                .build();

        return ApiResponseGenerator.success(res, HttpStatus.CREATED);
    }

    @GetMapping("/gallery")
    public ApiResponse<ApiResponse.SuccessBody<ImageListInfo>> getImage(HttpServletRequest request){
        Long memberId = findMemberByToken(request);
        List<Image> images = imageService.getImagesByMemberId(memberId);

        ImageListInfo res = ImageListInfo.builder()
                .images(images)
                .build();
        return ApiResponseGenerator.success(res, HttpStatus.OK);
    }

    private Long findMemberByToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            return jwtTokenProvider.getUserIdFromToken(token);
        } else {
            throw new IllegalArgumentException("Invalid or missing Authorization header");
        }
    }
}
