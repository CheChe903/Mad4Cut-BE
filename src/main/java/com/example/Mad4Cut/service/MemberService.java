package com.example.Mad4Cut.service;

import com.example.Mad4Cut.domain.Member;
import com.example.Mad4Cut.repository.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Member saveOrUpdateUser(String profile) {
        // 프로필 JSON 파싱
        String userId = extractUserIdFromProfile(profile);
        String email = extractEmailFromProfile(profile);
        String name = extractNameFromProfile(profile);

        log.debug("id: {}", userId);
        log.debug("email: {}", email);
        log.debug("name: {}", name);

        Member member = memberRepository.findByUserId(userId).orElse(null);

        if (member == null) {
            log.debug("Member not found. Creating new member.");
            member = Member.builder()
                    .userId(userId)
                    .email(email)
                    .nickname(name)
                    .build();
            member = memberRepository.save(member);
            log.debug("Saved new member with id: {}", member.getId());
        } else {
            log.debug("Member found with id: {}", member.getId());
        }

        return member;
    }

    private String extractUserIdFromProfile(String profile) {
        try {
            JsonNode jsonNode = objectMapper.readTree(profile);
            return jsonNode.path("response").path("id").asText();
        } catch (Exception e) {
            log.error("Failed to extract userId from profile", e);
            return null;
        }
    }

    private String extractEmailFromProfile(String profile) {
        try {
            JsonNode jsonNode = objectMapper.readTree(profile);
            return jsonNode.path("response").path("email").asText();
        } catch (Exception e) {
            log.error("Failed to extract email from profile", e);
            return null;
        }
    }

    private String extractNameFromProfile(String profile) {
        try {
            JsonNode jsonNode = objectMapper.readTree(profile);
            return jsonNode.path("response").path("name").asText();
        } catch (Exception e) {
            log.error("Failed to extract name from profile", e);
            return null;
        }
    }
}
