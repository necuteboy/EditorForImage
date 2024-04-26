package com.example.editorforimages.mapper;

import com.example.editorforimages.dto.Image;
import com.example.editorforimages.entity.ImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    List<Image> toImageList(List<ImageEntity> meta);

    @Mapping(source = "id", target = "imageId")
    @Mapping(source = "originalName", target = "filename")
    Image toImage(ImageEntity meta);
}
