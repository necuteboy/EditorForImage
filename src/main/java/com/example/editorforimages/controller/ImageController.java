package com.example.editorforimages.controller;

import com.example.editorforimages.dto.Image;
import com.example.editorforimages.dto.ImageResponse;
import com.example.editorforimages.dto.UiSuccessContainer;
import com.example.editorforimages.dto.UploadResponseImage;
import com.example.editorforimages.dto.UserDto;
import com.example.editorforimages.mapper.ImageMapper;
import com.example.editorforimages.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Image Controller", description = "Базовый CRUD API для работы с картинками")
public class ImageController {

    private final ImageService imageService;

    private final ImageMapper metaMapper;

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    @Operation(summary = "Загрузка нового изображения в систему",
            description = """
                    В рамках данного метода необходимо:
                    1. Провалидировать файл. Максимальный размер файла - 10Мб,\s
                    поддерживаемые расширения - png, jpeg.
                    1. Загрузить файл в S3 хранилище.
                    1. Сохранить в БД мета-данные файла - название и
                    размер; ИД файла в S3; ИД пользователя, которому файл принадлежит.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
                    content = {@Content(schema = @Schema(implementation = UploadResponseImage.class))}),
            @ApiResponse(responseCode = "400", description = "Файл не прошел валидацию",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))}),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))})
    })
    public ResponseEntity<?> uploadImage(
            @RequestPart("file") @Valid final MultipartFile image,
            @RequestBody final UserDto user) {
        var imageUUID = imageService.uploadImage(image, user.name());
        return ResponseEntity.ok(new UploadResponseImage(imageUUID));
    }

    @GetMapping("/image/{image-id}")
    @Operation(summary = "Скачивание файла по ИД",
            description = """
                    В рамках данного метода необходимо:
                    1. Проверить, есть ли такой файл в системе.
                    1. Проверить, доступен ли данный файл пользователю.
                    1. Скачать файл.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
                    content = {@Content(mediaType = "*/*", schema = @Schema(implementation = MultipartFile.class))}),
            @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))}),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))})
    })
    @SneakyThrows
    public ResponseEntity<?> downloadImage(@PathVariable("image-id") final UUID imageId,
                                           @RequestBody final UserDto user) {
        if (!imageService.getImageMeta(imageId).getAuthor().equals(user.id())) {
            return ResponseEntity.status(404)
                    .body(new UiSuccessContainer(false, "Image not found, or unavailable"));
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + URLEncoder.encode(imageService.getImageMeta(imageId).getOriginalName(),
                        StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(imageService.downloadImage(imageId));
    }

    @DeleteMapping("/image/{image-id}")
    @Operation(summary = "Удаление файла по ИД",
            description = """
                    В рамках данного метода необходимо:
                    1. Проверить, есть ли такой файл в системе.
                    1. Проверить, доступен ли данный файл пользователю.
                    1. Удалить файл.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))}),
            @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))}),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))})
    })
    public ResponseEntity<?> deleteImage(@PathVariable("image-id") final UUID imageId,
                                         @RequestBody final UserDto user) {
        if (!imageService.getImageMeta(imageId).getAuthor().equals(user.id())) {
            return ResponseEntity.status(404)
                    .body(new UiSuccessContainer(false, "Image not found, or unavailable"));
        }
        imageService.deleteImage(imageId);
        return ResponseEntity.ok(new UiSuccessContainer(true, null));
    }

    @GetMapping("/images")
    @Operation(summary = "Получение списка изображений, которые доступны пользователю",
            description = """
                    В рамках данного метода необходимо:
                    1. Получить мета-информацию о всех изображениях, которые доступны пользователю
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
                    content = {@Content(schema = @Schema(implementation = ImageResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
                    content = {@Content(schema = @Schema(implementation = UiSuccessContainer.class))})
    })
    public ResponseEntity<?> getImages(@RequestBody final UserDto user) {
        var imageMetaEntities = imageService.getImages(user.id());
        var images = metaMapper.toImageList(imageMetaEntities);
        return ResponseEntity.ok(new ImageResponse(images.toArray(new Image[0])));
    }

}
