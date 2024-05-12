package com.example.editorforimages.repository;

import com.example.editorforimages.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {

}
