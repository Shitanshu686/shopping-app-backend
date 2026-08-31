package com.shitanshu.shopping.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private final Path uploadDirectory =
            Paths.get("uploads/products");

    public String storeImage(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Image file cannot be empty"
            );
        }

        Files.createDirectories(uploadDirectory);

        String originalFileName =
                file.getOriginalFilename();

        String extension = "";

        if (originalFileName != null &&
            originalFileName.contains(".")) {

            extension =
                    originalFileName.substring(
                            originalFileName.lastIndexOf(".")
                    );
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path filePath =
                uploadDirectory.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/products/" + fileName;
    }
}