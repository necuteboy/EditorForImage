package com.example.editorforimages;

import com.example.editorforimages.config.BaseTest;
import com.example.editorforimages.entity.FilterType;
import com.example.editorforimages.entity.ImageEntity;
import com.example.editorforimages.entity.StatusResponse;
import com.example.editorforimages.entity.UserEntity;
import com.example.editorforimages.kafka.KafkaImageWipProducer;
import com.example.editorforimages.repository.ImageRepository;
import com.example.editorforimages.repository.RequestRepository;
import com.example.editorforimages.repository.UserRepository;
import com.example.editorforimages.service.AuthService;
import com.example.editorforimages.service.RequestService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestServiceTest extends BaseTest {
    @Autowired
    @InjectMocks
    private RequestService requestService;
    @Autowired
    private AuthService authService;

    @Autowired
    ImageRepository imageMetaRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "pass123!";
    private UserEntity user;

    @BeforeEach
    public void registerUser() {
        authService.register(new RegisterRequestDto(USERNAME, PASSWORD));
        user = userRepository.getReferenceById(USERNAME);
    }

    @AfterEach
    public void clear() {
        requestRepository.deleteAll();
        imageMetaRepository.deleteAll();

        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Mock
    private KafkaImageWipProducer imageWipProducer;

    @Test
    @ExtendWith(MockitoExtension.class)
    public void createRequestTest() {
        // given
        var imageId = UUID.randomUUID();
        imageMetaRepository.save(new ImageEntity(
                imageId,
                "image.png",
                "picture",
                1234L,
                user.getId()));

        // when
        var requestId = requestService.createRequest(imageId, USERNAME,
                new FilterType[] {FilterType.REVERS_COLORS}).getId();

        // then
        var request = requestService.getRequest(requestId, imageId);
        assertEquals(imageId, request.getOriginalImageId());
        assertNull(request.getModifiedImageId());
        assertEquals(StatusResponse.WIP, request.getStatus());
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    public void createRequestNoImageTest() {
        // when
        assertThrows(EntityNotFoundException.class, () -> requestService.createRequest(UUID.randomUUID(), USERNAME,
                new FilterType[]{FilterType.REVERS_COLORS}).getId());
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    public void getRequestNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () ->
                requestService.getRequest(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    public void getRequestWrongImageIdTest() {
        // given
        var imageId = UUID.randomUUID();
        imageMetaRepository.save(new ImageEntity(
                imageId,
                "image.png",
                "picture",
                1234L,
                user.getId()));
        var requestId = requestService.createRequest(imageId, USERNAME,
                new FilterType[] {FilterType.REVERS_COLORS}).getId();

        // then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                requestService.getRequest(requestId, UUID.randomUUID()));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    public void closeRequestTest() {
        // given
        var imageId = UUID.randomUUID();
        imageMetaRepository.save(new ImageEntity(
                imageId,
                "image.png",
                "picture",
                1234L,
                user.getId()));
        var requestId = requestService.createRequest(imageId, USERNAME,
                new FilterType[] {FilterType.REVERS_COLORS}).getId();

        // when
        var newImageId = UUID.randomUUID();
        requestService.closeRequest(requestId, newImageId);

        // then
        var request = requestService.getRequest(requestId, imageId);
        assertEquals(imageId, request.getOriginalImageId());
        assertEquals(newImageId, request.getModifiedImageId());
        assertEquals(StatusResponse.DONE, request.getStatus());
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    public void closeNonExistentRequestTest() {
        assertThrows(EntityNotFoundException.class, () ->
                requestService.closeRequest(UUID.randomUUID(), UUID.randomUUID()));
    }
}
