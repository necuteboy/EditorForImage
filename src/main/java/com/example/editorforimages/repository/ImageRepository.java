package com.example.editorforimages.repository;

import com.example.editorforimages.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {

    List<ImageEntity> findAllByAuthorName(String username);

}
