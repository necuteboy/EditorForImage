package com.example.editorforimages;

import com.example.editorforimages.config.BaseTest;
import com.example.editorforimages.dto.RegistrationUserDto;
import com.example.editorforimages.repository.ImageRepository;
import com.example.editorforimages.repository.UserRepository;
import com.example.editorforimages.service.AuthService;
import com.example.editorforimages.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ImageServiceTest extends BaseTest {

    @Autowired
    ImageService imageService;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository metaRepository;

    private static final String FILE_ORIG_NAME = "file.png";
    private static final String FILE_TEXT = "Hello, World!";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "pass123!";
    private static final String CONFIRM = "pass123!";

    @BeforeEach
    public void registerUser() {
        authService.createNewUser(new RegistrationUserDto(USERNAME, PASSWORD,CONFIRM));
    }

    @AfterEach
    public void cleanup() {
        for (var image : metaRepository.findAll())
            imageService.deleteImage(image.getId());
        metaRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void storeFile() {
        MockMultipartFile file = new MockMultipartFile(
                FILE_ORIG_NAME, FILE_ORIG_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                FILE_TEXT.getBytes());

        var id = imageService.uploadImage(file, USERNAME);

        var stream = imageService.downloadImage(id);
        assertArrayEquals(FILE_TEXT.getBytes(), stream.getInputStream().readAllBytes());

        var meta = imageService.getImageMeta(id);
        assertEquals(id, meta.getId());

        var imagesByUser = imageService.getImages(userRepository.findByName(USERNAME).getId());
        assertEquals(1, imagesByUser.size());
        assertEquals(id, imagesByUser.get(0).getId());
    }

    @Test
    public void deleteFile() {
        MockMultipartFile file = new MockMultipartFile(
                FILE_ORIG_NAME, FILE_ORIG_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                FILE_TEXT.getBytes());
        var id = imageService.uploadImage(file, USERNAME);

        imageService.deleteImage(id);

        assertThrows(EntityNotFoundException.class,
                () -> imageService.getImageMeta(id));
    }

    @Test
    public void downloadNonExisting() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> imageService.downloadImage(UUID.randomUUID()));
    }

    @Test
    public void deleteNonExisting() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> imageService.deleteImage(UUID.randomUUID()));
    }

}
