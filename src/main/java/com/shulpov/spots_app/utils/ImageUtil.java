package com.shulpov.spots_app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtil {

    public static String address;
    public static String port;

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public ImageUtil(@Value("${server.address}") String address, @Value("${server.port}") String port) {
        ImageUtil.address = address;
        ImageUtil.port = port;
    }

    public static String getUserImageDownloadUrl(Long imageId) {
        logger.atInfo().log("getUserImageDownloadUrl imageId={}", imageId);
        String url = String.format("%s:%s/api/image-service/download-user-image/%d", address, port, imageId);
        logger.atInfo().log("url={}", url);
        return url;
    }

    public static String getSpotImageDownloadUrl(Long imageId) {
        logger.atInfo().log("getSpotImageDownloadUrl imageId={}", imageId);
        String url = String.format("%s:%s/api/image-service/download-spot-image/%d", address, port, imageId);
        logger.atInfo().log("url={}", url);
        return url;
    }
}
