package com.example.Mad4Cut.controller;

import com.example.Mad4Cut.domain.dto.response.TestInfo;
import com.example.Mad4Cut.security.JwtTokenProvider;
import com.example.Mad4Cut.service.TestService;
import com.example.Mad4Cut.support.ApiResponse;
import com.example.Mad4Cut.support.ApiResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ApiResponse<ApiResponse.SuccessBody<TestInfo>> testexcute(HttpServletRequest request) {
        Long memberId = findMemberByToken(request);
        log.debug("Member ID: {}", memberId);
        String name = testService.execute(memberId);
        log.debug("Member Name: {}", name);

        TestInfo res = TestInfo.builder()
                .name(name)
                .build();

        return ApiResponseGenerator.success(res, HttpStatus.CREATED);
    }

    private Long findMemberByToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.debug("Authorization Header: {}", authorization);

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            return jwtTokenProvider.getUserIdFromToken(token);
        } else {
            throw new IllegalArgumentException("Invalid or missing Authorization header");
        }
    }
}
