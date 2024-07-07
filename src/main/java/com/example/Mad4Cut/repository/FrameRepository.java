package com.example.Mad4Cut.repository;

import com.example.Mad4Cut.domain.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Long> {
    List<Frame> findAll();
}