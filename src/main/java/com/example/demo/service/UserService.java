package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.entity.User;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final AttachmentRepository attachmentRepository;

    public ApiResponse<?> register(UserDTO dto) {
        System.err.println("\n\n\n\n" + dto.toString() + "\n\n\n\n");
        User user = new User();
        user.setFio(dto.getFio());
        user.setEmail(dto.getEmail());
        user.setFaoliyat(dto.getFaoliyat());
        user.setLavozim(dto.getLavozim());
        user.setTel(dto.getTel());
        user.setTashkilot(dto.getTashkilot());
        user.setResident(dto.isResident());
        if (dto.isResident()){
            Optional<Region> regionOptional = regionRepository.findById(dto.getRegionId());
            if (regionOptional.isEmpty()){
                return ApiResponse.builder().
                        message("Region not found!").
                        status(400).
                        success(false).
                        build();
            }
            user.setRegion(regionOptional.get());
        }
        else {
            Optional<Country> countryOptional = countryRepository.findById(dto.getCountryId());
            if (countryOptional.isEmpty()){
                return ApiResponse.builder().
                        message("Country not found!").
                        status(400).
                        success(false).
                        build();
            }
            user.setCountry(countryOptional.get());
        }
        User save = userRepository.save(user);
        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            MultipartFile photo = dto.getPhoto();
            String fileType = photo.getOriginalFilename().substring(photo.getOriginalFilename().indexOf("."));
//            String outputPath = "projects\\photos\\" + save.getId() + fileType;
            String outputPath = "src\\main\\resources\\photos\\" + save.getId() + fileType;
            try {
                BufferedImage image = bytesToImage(photo.getBytes());
                saveImage(image, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Attachment attachment = new Attachment();
            attachment.setSize(photo.getSize());
            try {
                attachment.setBytes(photo.getBytes());
            } catch (IOException e) {
                return ApiResponse.builder().
                        message("Photo content type not supported!").
                        status(400).
                        success(false).
                        build();
            }
            attachment.setContentType(photo.getContentType());
            attachment.setOriginalName(photo.getOriginalFilename());
            save.setAttachment(attachmentRepository.save(attachment));
            save.setPhoto(true);
        }else {
            save.setPhoto(false);
        }
        userRepository.save(save);
        return ApiResponse.builder().
                message("Registered").
                success(true).
                status(201).
                build();
    }

    public static BufferedImage bytesToImage(byte[] imageBytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bis);
    }

    public static void saveImage(BufferedImage image, String outputPath) throws IOException {
        File outputImage = new File(outputPath);
        ImageIO.write(image, "jpg", outputImage); // Change the format as needed
    }

    public ResponseEntity<?> getPhoto(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!!!");
        }
        User user = userOptional.get();
        if (!user.isPhoto() || user.getAttachment() == null){
            return ResponseEntity.badRequest().body("Photo not found!!!");
        }
        Attachment attachment = user.getAttachment();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachment.getContentType()))
                .contentLength(attachment.getSize())
                .body(attachment.getBytes());
    }
}
