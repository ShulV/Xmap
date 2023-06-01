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

/**
 * Класс,отвечающий за загрузку/скачивание/удаление изображений в/из директории на сервере. Класс-@Component.
 * @author Victor Shulpov "vshulpov@gmail.com"
 * @version 1.0
 * @since 1.0
 */
@Component
public class ImageManager implements FileManager {

    /**
     * Загрузка файла на диск
     * @param bytes файл, переданный в виде массива байт
     * @param dirPath абсолютный путь до загружаемого файла
     * @param genName имя загружаемого файла
     * @throws IOException исключение, связанное с проблемой потоков ввода/вывода при работе с изображениями
     * (например, закончилось место на диске)
     */
    @Override
    public void upload(byte[] bytes, String dirPath, String genName) throws IOException {
        Path path = getAbsolutePath(dirPath, genName);
        Path file = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(file.toString())) {
            stream.write(bytes);
        }
    }

    /**
     * Скачивание изображения с диска
     * @param dirPath абсолютный путь до изображения
     * @param genName название изображения
     * @return ресурс (изображение)
     * @throws IOException исключение, связанное с проблемой потоков ввода/вывода при работе с изображениями
     * (например, закончилось место на диске)
     */
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

    /**
     * Удаление файла с диска
     * @param dirPath абсолютный путь до изображения
     * @param genName название изображения
     * @throws IOException исключение, связанное с проблемой потоков ввода/вывода при работе с изображениями
     * (например, закончилось место на диске)
     */
    @Override
    public void delete(String dirPath, String genName) throws IOException {
        Path path = getAbsolutePath(dirPath, genName);
        Files.delete(path);
    }

    /**
     * Получение абсолютного пути, включающего в себя название файла
     * @param dirPath абсолютный путь до изображения
     * @param genName название изображения
     * @return экземпляр объекта класса Path (путь до изображения включительно)
     */
    public Path getAbsolutePath(String dirPath, String genName) {
        String absPath =  Paths.get(new File("").getAbsolutePath(), dirPath).toString();
        return Paths.get(absPath, genName);
    }
}
