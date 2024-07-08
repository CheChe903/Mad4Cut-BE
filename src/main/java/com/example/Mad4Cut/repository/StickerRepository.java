package com.example.Mad4Cut.repository;

import com.example.Mad4Cut.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {

    List<Sticker> findByShared(boolean shared);
    List<Sticker> findByMemberId(Long memberId);

}
