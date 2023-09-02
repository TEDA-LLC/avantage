package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PhotoDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.Country;
import com.example.demo.entity.Region;
import com.example.demo.entity.User;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
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
        if (dto.isResident()) {
            Optional<Region> regionOptional = regionRepository.findById(dto.getRegionId());
            if (regionOptional.isEmpty()) {
                return ApiResponse.builder().
                        message("Region not found!").
                        status(400).
                        success(false).
                        build();
            }
            user.setRegion(regionOptional.get());
        } else {
            Optional<Country> countryOptional = countryRepository.findById(dto.getCountryId());
            if (countryOptional.isEmpty()) {
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
            String outputPath = "photos/" + save.getId() + fileType;
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                BufferedImage image;
                if (dto.getImg() != null && !dto.getImg().isEmpty()) {
                    image = bytesToImage(decoder.decode(dto.getImg()));
                } else {
                    image = bytesToImage(photo.getBytes());
                }
                saveImage(image, outputPath, fileType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Attachment attachment = new Attachment();
            attachment.setSize(photo.getSize());
//            attachment.setBytes(photo.getBytes());
            try {
                attachment.setBytes(photo.getBytes());
            } catch (IOException e) {
                return ApiResponse.builder().
                        message("Photo type is not supported!").
                        status(400).
                        success(false).
                        build();
            }
            attachment.setContentType(photo.getContentType());
            attachment.setOriginalName(photo.getOriginalFilename());
            save.setAttachment(attachmentRepository.save(attachment));
            save.setPhoto(true);
        } else {
            save.setPhoto(false);
        }
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(save.getQrCode(), BarcodeFormat.QR_CODE, 500, 500);
        } catch (WriterException e) {
            return ApiResponse.builder().
                    message("Something went wrong when qrcode generated!!").
                    status(400).
                    success(false).
                    build();
        }
        String qrCodeOutputPath = "photos/qrcode/" + save.getId() + ".jpg";

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "JPEG", pngOutputStream);
        } catch (IOException e) {
            return ApiResponse.builder().
                    message("Something went wrong!!").
                    status(400).
                    success(false).
                    build();
        }
        byte[] jpgData = pngOutputStream.toByteArray();
        BufferedImage qrcode;
        try {
             qrcode = bytesToImage(jpgData);
        } catch (IOException e) {
            return ApiResponse.builder().
                    message("Something went wrong when qrcode parsed to image!!").
                    status(400).
                    success(false).
                    build();
        }
        try {
            saveImage(qrcode, qrCodeOutputPath, ".jpg");
        } catch (IOException e) {
            return ApiResponse.builder().
                    message("Something went wrong when qrcode saved!!").
                    status(400).
                    success(false).
                    build();
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

    public static void saveImage(BufferedImage image, String outputPath, String fileType) throws IOException {
        File outputImage = new File(outputPath);
        ImageIO.write(image, fileType.substring(1), outputImage);
    }

    public ResponseEntity<?> getPhoto(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!!!");
        }
        User user = userOptional.get();
        if (!user.isPhoto() || user.getAttachment() == null) {
            return ResponseEntity.badRequest().body("Photo not found!!!");
        }
        Attachment attachment = user.getAttachment();
        return ResponseEntity.ok()
                .contentType(attachment.getContentType().startsWith("image/") ? MediaType.valueOf(attachment.getContentType())
                        : MediaType.valueOf("image/" + attachment.getOriginalName().substring(attachment.getOriginalName().indexOf(".") + 1)))
                .contentLength(attachment.getSize())
                .body(attachment.getBytes());
    }

    public ApiResponse<User> getOne(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ApiResponse.<User>builder().
                    message("User not found!!!").
                    status(400).
                    success(false).
                    build();
        }
        return ApiResponse.<User>builder().
                message("Here!!!").
                status(200).
                success(true).
                data(userOptional.get()).
                build();
    }

    public ApiResponse<User> getByQrcode(String qr) {
        Optional<User> userOptional = userRepository.findByQrCode(qr);
        if (userOptional.isEmpty()) {
            return ApiResponse.<User>builder().
                    message("User not found!!!").
                    status(400).
                    success(false).
                    build();
        }
        return ApiResponse.<User>builder().
                message("Here!!!").
                status(200).
                success(true).
                data(userOptional.get()).
                build();
    }
}
