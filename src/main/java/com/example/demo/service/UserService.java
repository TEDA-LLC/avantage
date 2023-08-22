package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.entity.User;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

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

    @SneakyThrows
    public ApiResponse<?> register(UserDTO dto) {
        User user = new User();
        user.setFio(dto.getFio());
        user.setEmail(dto.getEmail());
        user.setFaoliyat(dto.getFaoliyat());
        user.setLavozim(dto.getLavozim());
        user.setTel(dto.getTel());
        user.setTashkilot(dto.getTashkilot());
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
        }
        User save = userRepository.save(user);
        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            String outputPath = "src\\main\\resources\\photos\\" + save.getId() + ".jpg";
            try {
                BufferedImage image = bytesToImage(dto.getPhoto().getBytes());
                saveImage(image, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}