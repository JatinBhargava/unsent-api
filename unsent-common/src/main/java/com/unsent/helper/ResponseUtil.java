package com.unsent.helper;

import com.unsent.entity.ApiResponse;

import java.util.List;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(
            String message,
            T data
    ) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<List<T>> successList(
            String message,
            List<T> data
    ) {
        return ApiResponse.<List<T>>builder()
                .message(message)
                .count((long) data.size())
                .data(data)
                .build();
    }
}
