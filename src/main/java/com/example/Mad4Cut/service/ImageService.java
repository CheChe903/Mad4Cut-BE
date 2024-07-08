package com.example.Mad4Cut.service;

import com.example.Mad4Cut.domain.Image;
import com.example.Mad4Cut.domain.Member;
import com.example.Mad4Cut.repository.ImageRepository;
import com.example.Mad4Cut.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class ImageService {

    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    @Value("${file.upload-image-dir}")
    private String uploadDir;

    @Autowired
    public ImageService(ImageRepository imageRepository, MemberRepository memberRepository) {
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
    }

    public String saveImage(MultipartFile file, Long memberId) throws IOException {
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

        // 데이터베이스에 파일 URL 저장
        String fileUrl = "/images/" + fileName;

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Image image = Image.builder()
                    .url(fileUrl)
                    .member(member)
                    .build();
            imageRepository.save(image);
            return fileUrl;
        } else {
            throw new IllegalArgumentException("Member not found with ID: " + memberId);
        }
    }

    public List<Image> getImagesByMemberId(Long memberId) {
        return imageRepository.findByMemberId(memberId);
    }
}
