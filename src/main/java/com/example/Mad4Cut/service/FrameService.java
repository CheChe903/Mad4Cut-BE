package com.example.Mad4Cut.service;

import com.example.Mad4Cut.domain.Frame;
import com.example.Mad4Cut.domain.Image;
import com.example.Mad4Cut.repository.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FrameService {


    @Autowired
    private final FrameRepository frameRepository;

    public FrameService(FrameRepository frameRepository) {
        this.frameRepository = frameRepository;
    }

    public List<Frame> getAllFrames() {
        return frameRepository.findAll();
    }


}
