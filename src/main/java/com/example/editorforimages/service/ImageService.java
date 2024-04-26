package com.example.editorforimages.service;

import com.example.editorforimages.entity.ImageEntity;
import com.example.editorforimages.exceptions.FileReadException;
import com.example.editorforimages.exceptions.FileWriteException;
import com.example.editorforimages.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageStorageService imageStorageService;
    private final ImageRepository metaRepository;
    private final UserService userService;

    public UUID uploadImage(MultipartFile image, String username) throws ConstraintViolationException {
        MinioFileStorageService.FileSaveResult res;
        try {
            res = imageStorageService.saveFile(image);
        } catch (FileWriteException e) {
            throw new RuntimeException(e);
        }
        var user = userService.findByUserName(username).getId();
        ImageEntity meta = new ImageEntity();
        meta.setId(res.getSavedFilename());
        meta.setOriginalName(image.getOriginalFilename());
        meta.setSize(image.getSize());
        meta.setAuthor(user);

        metaRepository.save(meta);
        return meta.getId();

    }
    public InputStreamResource downloadImage(UUID imageId) throws FileReadException {
        if (!metaRepository.existsById(imageId))
            throw new EntityNotFoundException("No image with id: " + imageId);

        InputStream fileInputStream = imageStorageService.get(imageId);
        return new InputStreamResource(fileInputStream);
    }

    public ImageEntity getImageMeta(UUID imageID) {
        return imageStorageService.getMeta(imageID);
    }

    @SneakyThrows
    public void deleteImage(UUID imageID) {
        if (!metaRepository.existsById(imageID))
            throw new EntityNotFoundException("No image with id: " + imageID);
        imageStorageService.delete(imageID.toString());
        metaRepository.deleteById(imageID);
    }

    public List<ImageEntity> getImages(Long id) {
        return metaRepository.findAllByAuthor(id);
    }
}

