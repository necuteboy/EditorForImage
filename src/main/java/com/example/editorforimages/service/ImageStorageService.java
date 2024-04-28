package com.example.editorforimages.service;

import com.example.editorforimages.entity.ImageEntity;
import com.example.editorforimages.exceptions.FileReadException;
import com.example.editorforimages.exceptions.FileWriteException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface ImageStorageService {
    MinioFileStorageService.FileSaveResult saveFile(MultipartFile file) throws FileWriteException;

    InputStream get(UUID id) throws FileReadException;

    ImageEntity getMeta(UUID id);

    void delete(String filename) throws FileWriteException;
}
