package com.example.demo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.entity.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private String fio;
    private String email;
    private String tashkilot;
    private String lavozim;
    private String faoliyat;
    private String tel;
    private Long countryId;
    private Long regionId;
    private boolean resident;

    private PhotoDTO photo;

}