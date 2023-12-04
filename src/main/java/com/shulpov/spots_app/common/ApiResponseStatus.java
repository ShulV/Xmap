package com.shulpov.spots_app.common;

/**
 * Общий класс для всех responses в rest api
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
public enum ApiResponseStatus {
    /**
     * успешно
     */
    SUCCESS,
    /**
     * ошибка валидации (для создания или изменения объектов)
     */
    VALIDATION_ERROR,
    /**
     * ошибка сервера
     */
    BACKEND_ERROR,
    /**
     * Ошибка клиентской стороны (должна возникать, когда на клиенте реализована неправильная логика)
     */
    CLIENT_ERROR
}
