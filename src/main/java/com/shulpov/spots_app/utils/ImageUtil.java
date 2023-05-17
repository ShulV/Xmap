package com.shulpov.spots_app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtil {

    public static String address;
    public static String port;

    public ImageUtil(@Value("${server.address}") String address, @Value("${server.port}") String port) {
        this.address = address;
        this.port = port;
    }

    public static String getUserImageDownloadUrl(Long imageId) {
        return String.format("%s:%s/api/image-service/download-user-image/%d", address, port, imageId);
    }

    public static String getSpotImageDownloadUrl(Long imageId) {
        return String.format("%s:%s/api/image-service/download-spot-image/%d", address, port, imageId);
    }
}
