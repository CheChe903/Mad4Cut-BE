package com.example.Mad4Cut.service;

import com.example.Mad4Cut.domain.Member;
import com.example.Mad4Cut.domain.Sticker;
import com.example.Mad4Cut.repository.MemberRepository;
import com.example.Mad4Cut.repository.StickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StickerService {

    private final StickerRepository stickerRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Value("${file.upload-sticker-dir}")
    private String uploadDir;

    @Value("${removebg.api-key}")
    private String apiKey;

    @Autowired
    public StickerService(StickerRepository stickerRepository, MemberRepository memberRepository, RestTemplate restTemplate) {
        this.stickerRepository = stickerRepository;
        this.memberRepository = memberRepository;
        this.restTemplate = restTemplate;
    }

    public String saveSticker(MultipartFile file, Long memberId) throws IOException {
        // 파일 저장 경로 설정
        String staticDir = new File("src/main/resources/static").getAbsolutePath();
        File uploadDirFile = new File(staticDir, uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // 유니크 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirFile.getAbsolutePath(), fileName);
        Files.write(filePath, file.getBytes());

        // 배경 제거를 위해 remove.bg API 호출
        byte[] processedImage = removeBackground(filePath);

        // 배경이 제거된 이미지 저장
        String processedFileName = UUID.randomUUID().toString() + "_bg_removed_" + file.getOriginalFilename();
        Path processedFilePath = Paths.get(uploadDirFile.getAbsolutePath(), processedFileName);
        Files.write(processedFilePath, processedImage);

        // 데이터베이스에 파일 URL 저장
        String fileUrl = "/stickers/" + processedFileName;

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Sticker sticker = Sticker.builder()
                    .url(fileUrl)
                    .member(member)
                    .shared(false)
                    .build();
            stickerRepository.save(sticker);

            return sticker.getUrl();
        } else {
            throw new IllegalArgumentException("Member not found with ID: " + memberId);
        }
    }

    private byte[] removeBackground(Path filePath) throws IOException {
        String removeBgApiUrl = "https://api.remove.bg/v1.0/removebg";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-Api-Key", apiKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_file", new FileSystemResource(filePath));
        body.add("size", "auto");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(removeBgApiUrl, HttpMethod.POST, requestEntity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new IOException("Failed to remove background: " + response.getStatusCode());
        }
    }

    public List<Sticker> getStickersByShared(boolean shared) {
        return stickerRepository.findByShared(shared);
    }

    public List<Sticker> getPersonalStickers(Long memberId) {
        return stickerRepository.findByMemberId(memberId);
    }

    public Sticker shareSticker(Long stickerId, Long memberId) {
        Optional<Sticker> stickerOptional = stickerRepository.findById(stickerId);
        if (stickerOptional.isPresent()) {
            Sticker sticker = stickerOptional.get();
            sticker.setShared(true);
            return stickerRepository.save(sticker);
        } else {
            throw new IllegalArgumentException("Sticker not found or does not belong to the member.");
        }
    }
}
