package com.example.demo.controller;


import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser (@PathVariable Integer id){
        ApiResponse<User> response = userService.getOne(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/qrcode/{qr}")
    public ResponseEntity<?> getUserByQrcode(@PathVariable String qr){
        ApiResponse<User> response = userService.getByQrcode(qr);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
