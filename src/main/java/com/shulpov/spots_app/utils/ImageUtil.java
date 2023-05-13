package com.shulpov.spots_app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class ImageUtil {
    @Value("server.address")
    public static String address;

    @Value("server.port")
    public static int port;

    public static String getUserImageUrl(Long imageId) {
        return String.format("%s:%d/api/image-service/download-user-image/%d", address, port, imageId);
    }

    public static String getSpotImageUrl(Long imageId) {
        return String.format("%s:%d/api/image-service/download-user-image/%d", address, port, imageId);
    }
}
