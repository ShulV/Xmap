package com.shulpov.spots_app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Класс, помогающий получать ссылки на изображения. Класс-@Component.
 * @author Victor Shulpov "vshulpov@gmail.com"
 * @version 1.0
 * @since 1.0
 */
@Component
public class ImageUtil {

    /** адрес сервера из properties-файла */
    public static String address;

    /** порт сервера из properties-файла */
    public static String port;

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * Конструктор с автоподстановкой значений параметров из properties-файла
     * @param address адрес сервера
     * @param port порт сервера
     */

    public ImageUtil(@Value("${server.address}") String address, @Value("${server.port}") String port) {
        ImageUtil.address = address;
        ImageUtil.port = port;
    }

    /**
     *
     * @param imageId идентификатор изображения
     * @return url для скачивания изображения пользователя
     */
    public static String getUserImageDownloadUrl(Long imageId) {
        logger.atInfo().log("getUserImageDownloadUrl imageId={}", imageId);
        String url = String.format("%s:%s/api/image-service/download-user-image/%d", address, port, imageId);
        logger.atInfo().log("url={}", url);
        return url;
    }

    /**
     *
     * @param imageId идентификатор изображения
     * @return url для скачивания изображения спота
     */
    public static String getSpotImageDownloadUrl(Long imageId) {
        logger.atInfo().log("getSpotImageDownloadUrl imageId={}", imageId);
        String url = String.format("%s:%s/api/image-service/download-spot-image/%d", address, port, imageId);
        logger.atInfo().log("url={}", url);
        return url;
    }
}
