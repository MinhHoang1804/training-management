package com.g96.ftms.service.file.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobStorageException;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.service.file.IImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.azure.storage.blob.BlobServiceClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService implements IImageStorageService {
    private final BlobServiceClient blobServiceClient;

    @Override
    public String uploadImage(String containerName, String originalImageName, InputStream data, long length) throws IOException {
        try {
            // Get the BlobContainerClient object to interact with the container
            BlobContainerClient blobContainerClient = this.blobServiceClient.getBlobContainerClient(containerName);

            // Rename the image file to a unique name
            String newImageName = UUID.randomUUID().toString() + originalImageName.substring(originalImageName.lastIndexOf("."));

            // Get the BlobClient object to interact with the specified blob
            BlobClient blobClient = blobContainerClient.getBlobClient(newImageName);

            // Upload the image file to the blob
            blobClient.upload(data, length, true);

            return blobClient.getBlobUrl();
        } catch (BlobStorageException e) {
            e.printStackTrace();
            throw new AppException(HttpStatus.GATEWAY_TIMEOUT, ErrorCode.UPLOAD_FILE_FAILED);
        }
    }
}
