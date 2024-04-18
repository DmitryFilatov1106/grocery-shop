package ru.fildv.groceryshop.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
public class ImageService {
    @Value("${app.image.product}")
    private String folderProduct;

    @Value("${app.image.user}")
    private String folderUser;

    @SneakyThrows
    void upload(String imagePath, InputStream content, ImageType type) {
        Path fullImagePath = switch (type) {
            case USER -> Path.of(folderUser, imagePath);
            case PRODUCT -> Path.of(folderProduct, imagePath);
        };
        try (content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imageName, boolean type) {
        Path fullImagePath = type ? Path.of(folderProduct, imageName) : Path.of(folderUser, imageName);
        return Files.exists(fullImagePath) ? Optional.of(Files.readAllBytes(fullImagePath)) : Optional.empty();
    }

    public static enum ImageType {
        USER, PRODUCT
    }
}