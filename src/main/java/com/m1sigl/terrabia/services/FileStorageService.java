package com.m1sigl.terrabia.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    // Récupère le chemin depuis application.properties
    public FileStorageService(@Value("${terrabia.upload.dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de créer le dossier de stockage.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Nettoie le nom du fichier
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Génère un nom unique (ex: image.jpg -> 550e8400-e29b...jpg)
        String extension = "";
        if (originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        try {
            if(uniqueFileName.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide " + uniqueFileName);
            }
            // Copie le fichier
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors du stockage du fichier " + uniqueFileName, ex);
        }
    }
}