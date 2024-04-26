package com.example.editorforimages.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "image_meta")
@Setter
@Getter
public class ImageEntity {
    @Id
    private UUID id;

    @Column(name = "origin_name")
    private String originalName;

    private String filename;

    private Long size;


    @Column(name = "user_id")
    private Long author;
}
