package com.g96.ftms.service.file;

import java.io.IOException;
import java.io.InputStream;

public interface IImageStorageService {
    String uploadImage(String containerName, String originalImageName, InputStream data, long length) throws IOException;
}
