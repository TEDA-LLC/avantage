package com.example.demo.controller;


import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@ModelAttribute UserDTO dto){
        ApiResponse<?> response = userService.register(dto);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @GetMapping("/getPhoto/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable Integer id){
        return userService.getPhoto(id);
    }

}
