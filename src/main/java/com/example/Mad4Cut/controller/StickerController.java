package com.example.Mad4Cut.controller;

import com.example.Mad4Cut.domain.Sticker;
import com.example.Mad4Cut.domain.dto.request.ShareStickerRequest;
import com.example.Mad4Cut.domain.dto.response.ImageInfo;
import com.example.Mad4Cut.domain.dto.response.SharedStickerInfo;
import com.example.Mad4Cut.domain.dto.response.StickerInfo;
import com.example.Mad4Cut.domain.dto.response.StickerListInfo;
import com.example.Mad4Cut.security.JwtTokenProvider;
import com.example.Mad4Cut.service.StickerService;
import com.example.Mad4Cut.support.ApiResponse;
import com.example.Mad4Cut.support.ApiResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sticker")
public class StickerController {

    private final StickerService stickerService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public StickerController(StickerService stickerService ,JwtTokenProvider jwtTokenProvider) {
        this.stickerService = stickerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/save")
    public ApiResponse<ApiResponse.SuccessBody<StickerInfo>> uploadSticker(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws  IOException {
        Long memberId = findMemberByToken(request);
        String fileUrl = stickerService.saveSticker(file, memberId);

        StickerInfo res = StickerInfo.builder()
                .id(memberId)
                .url(fileUrl)
                .build();
        return ApiResponseGenerator.success(res,HttpStatus.CREATED);
    }

    @GetMapping("/shared")
    public ApiResponse<ApiResponse.SuccessBody<StickerListInfo>> getSharedSticker() {
        List<Sticker> sharedStickers = stickerService.getStickersByShared(true);

        StickerListInfo res = StickerListInfo.builder()
                .stickers(sharedStickers)
                .build();

        return ApiResponseGenerator.success(res, HttpStatus.OK);
    }
    @GetMapping("/personal")
    public ApiResponse<ApiResponse.SuccessBody<StickerListInfo>> getPersonalSticker(HttpServletRequest request) {

        Long memberId = findMemberByToken(request);
        List<Sticker> personalStickers = stickerService.getPersonalStickers(memberId);

        StickerListInfo res = StickerListInfo.builder()
                .stickers(personalStickers)
                .build();

        return ApiResponseGenerator.success(res, HttpStatus.OK);
    }

    @PostMapping("/share")
    public ApiResponse<ApiResponse.SuccessBody<SharedStickerInfo>> SharedSticker(
            @RequestBody @Valid ShareStickerRequest requestData, HttpServletRequest request) {

        Long memberId = findMemberByToken(request);
        Sticker sticker = stickerService.shareSticker(memberId, requestData.getStickerId());

        SharedStickerInfo res =SharedStickerInfo.builder()
                .sticker(sticker)
                .build();

        return ApiResponseGenerator.success(res,HttpStatus.CREATED);
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
