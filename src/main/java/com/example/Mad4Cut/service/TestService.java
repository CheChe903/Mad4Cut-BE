package com.example.Mad4Cut.service;


import com.example.Mad4Cut.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberRepository memberRepository;

    @Transactional
    public String execute(Long memberId) {
        // 추가 로그를 통해 문제 원인 파악
        try {
            String name = memberRepository.findNicknameById(memberId);
            if (name == null) {
                throw new IllegalArgumentException("Member not found with ID: " + memberId);
            }
            return name;
        } catch (Exception e) {
            // 예외 로그 기록
            throw new RuntimeException("Failed to execute transaction", e);
        }
    }
}
