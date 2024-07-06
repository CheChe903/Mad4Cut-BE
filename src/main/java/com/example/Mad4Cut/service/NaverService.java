package com.example.Mad4Cut.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NaverService {

    private final String NAVER_PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";

    public String getNaverProfile(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(NAVER_PROFILE_API_URL, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}

