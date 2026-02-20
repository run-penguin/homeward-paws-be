package com.hp_be.common.dto;

public record ApiResDTO<T> (boolean success, String message, T data) {

    public static <T> ApiResDTO<T> ok(String message) {
        return new ApiResDTO<>(true, message, null);
    }

    public static <T> ApiResDTO<T> ok(String message, T data) {
        return new ApiResDTO<>(true, message, data);
    }

    public static <T> ApiResDTO<T> error(String message) {
        return new ApiResDTO<>(false, message, null);
    }
}
