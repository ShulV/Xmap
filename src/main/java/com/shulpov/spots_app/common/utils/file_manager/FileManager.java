package com.shulpov.spots_app.common.utils.file_manager;

import org.springframework.core.io.Resource;
import java.io.IOException;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
public interface FileManager {
    void upload(byte[] bytes, String dirPath, String genName) throws IOException;
    Resource download(String dirPath, String genName) throws IOException;
    void delete(String dirPath, String genName) throws IOException;
}
