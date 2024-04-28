package com.example.editorforimages.service;

import com.example.editorforimages.entity.ImageEntity;
import com.example.editorforimages.exceptions.FileReadException;
import com.example.editorforimages.exceptions.FileWriteException;
import com.example.editorforimages.repository.ImageRepository;
import com.example.editorforimages.repository.MinioFileStorage;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioFileStorageService implements ImageStorageService {
    private final MinioFileStorage imageStorage;
    private final ImageRepository metaRepository;

    public FileSaveResult saveFile(final MultipartFile file) throws FileWriteException {

        UUID generatedFileName = UUID.randomUUID();
        try {
            imageStorage.saveObject(String.valueOf(generatedFileName), file.getSize(), file.getInputStream());
        } catch (FileWriteException | IOException e) {
            throw new FileWriteException(e);
        }

        return new FileSaveResult(file.getOriginalFilename(), generatedFileName);
    }

    @Override
    public InputStream get(final UUID id) throws FileReadException {
        return imageStorage.getObject(String.valueOf(id));
    }

    @Override
    public ImageEntity getMeta(final UUID id) {
        return metaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Image not found"));
    }

    @Override
    public void delete(final String filename) throws FileWriteException {
        imageStorage.deleteObject(filename);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class FileSaveResult {
        private String originalName;
        private UUID savedFilename;
    }
}
