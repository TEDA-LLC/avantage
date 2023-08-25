package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionRepository regionRepository;
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(ApiResponse.builder().
                message("Here").
                status(200).
                success(true).
                data(regionRepository.findAll()).
                build());
    }

}
