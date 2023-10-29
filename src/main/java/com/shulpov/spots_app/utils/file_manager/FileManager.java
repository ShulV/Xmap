package com.shulpov.spots_app.utils.file_manager;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface FileManager {
    void upload(byte[] bytes, String dirPath, String genName) throws IOException;
    Resource download(String dirPath, String genName) throws IOException;
    void delete(String dirPath, String genName) throws IOException;
}
