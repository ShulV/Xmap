package com.shulpov.spots_app.file_manager;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageManager implements FileManager {

    //загрузка файла на диск
    @Override
    public void upload(byte[] bytes, String dirPath, String genName) throws IOException {
        Path path = getAbsolutePath(dirPath, genName);
        Path file = Files.createFile(path);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file.toString());
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }

    //скачивание файла с диска
    @Override
    public Resource download(String dirPath, String genName) throws IOException {
        Path path = getAbsolutePath(dirPath, genName);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }

    //удаление файла с диска
    @Override
    public void delete(String dirPath, String genName) throws IOException {
        Path path = getAbsolutePath(dirPath, genName);
        Files.delete(path);
    }

    public Path getAbsolutePath(String directoryPath, String genName) {
        String absPath =  Paths.get(new File("").getAbsolutePath(), directoryPath).toString();
        return Paths.get(absPath, genName);
    }
}
