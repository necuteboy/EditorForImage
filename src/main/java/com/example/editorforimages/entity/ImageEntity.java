package com.example.editorforimages.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "image_meta")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
