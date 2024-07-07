package com.example.Mad4Cut.controller;

import com.example.Mad4Cut.domain.dto.request.NaverTokenRequest;
import com.example.Mad4Cut.domain.Member;
import com.example.Mad4Cut.domain.dto.response.AccessNaverInfo;
import com.example.Mad4Cut.security.JwtTokenProvider;
import com.example.Mad4Cut.service.MemberService;
import com.example.Mad4Cut.service.NaverService;
import com.example.Mad4Cut.service.TokenService;
import com.example.Mad4Cut.support.ApiResponse;
import com.example.Mad4Cut.support.ApiResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/member")

@Slf4j
public class AuthController {

    @Autowired
    private NaverService naverService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MemberService memberService;


    @PostMapping("/login")
    public ApiResponse<ApiResponse.SuccessBody<AccessNaverInfo>> authenticateWithNaver(@RequestBody NaverTokenRequest request) {
        String naverProfile = naverService.getNaverProfile(request.getToken());
        // 네이버 프로필 정보 기반으로 사용자 저장 또는 업데이트
        Member member = memberService.saveOrUpdateUser(naverProfile);

        // 자체 JWT 토큰 생성
        String jwtToken = tokenService.createToken(member.getId());

        // 응답 데이터 구성
        AccessNaverInfo res = AccessNaverInfo.builder()
                .token(jwtToken)
                .build();

        return ApiResponseGenerator.success(res, HttpStatus.CREATED);
    }

    @GetMapping("/generate-token")
    public String generateToken(@RequestParam Long id) {
        return tokenService.createToken(id);
    }
}


