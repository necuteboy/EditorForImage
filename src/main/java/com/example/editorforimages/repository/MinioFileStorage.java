package com.example.editorforimages.repository;

import com.example.editorforimages.exceptions.FileReadException;
import com.example.editorforimages.exceptions.FileWriteException;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinioFileStorage {
    private final MinioClient minioClient;

    public ObjectWriteResponse saveObject(final String objectName,
                                          final Long size, final InputStream object) throws FileWriteException {
        try {
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket("image-storage")
                    .object(objectName)
                    .stream(object, size, -1).build());
        } catch (Exception e) {
            throw new FileWriteException(e);
        }
    }

    public boolean isObjectExist(final String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket("image-storage")
                    .object(objectName).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getObject(final String objectName) throws FileReadException {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket("image-storage")
                    .object(objectName).build());
        } catch (Exception e) {
            throw new FileReadException(e);
        }
    }

    public void deleteObject(final String objectName) throws FileWriteException {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("image-storage")
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new FileWriteException(e);
        }
    }

    public List<String> getObjectList() {
        var iterable = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("image-storage").build());

        List<String> objectNameList = new LinkedList<>();
        for (Result<Item> result : iterable) {
            try {
                objectNameList.add(result.get().objectName());
            } catch (Exception ignored) {
            }
        }
        return objectNameList;
    }
}
