package com.example.Mad4Cut.controller;

import com.example.Mad4Cut.domain.Frame;
import com.example.Mad4Cut.domain.Image;
import com.example.Mad4Cut.domain.dto.response.FrameListInfo;
import com.example.Mad4Cut.domain.dto.response.ImageListInfo;
import com.example.Mad4Cut.security.JwtTokenProvider;
import com.example.Mad4Cut.service.FrameService;
import com.example.Mad4Cut.service.ImageService;
import com.example.Mad4Cut.support.ApiResponse;
import com.example.Mad4Cut.support.ApiResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/api/v1")
@Slf4j
public class FrameController {

    private FrameService frameService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FrameController(FrameService frameService, JwtTokenProvider jwtTokenProvider) {
        this.frameService = frameService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @GetMapping("/frames")
    public ApiResponse<ApiResponse.SuccessBody<FrameListInfo>> getImage(HttpServletRequest request){
        Long memberId = findMemberByToken(request);
        List<Frame> frames = frameService.getAllFrames();

        for(Frame f : frames)
        {
            log.debug("frame url:{}", f.getUrl());
        }

        FrameListInfo res = FrameListInfo.builder()
                .frames(frames)
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
