package com.unsent.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"message", "count", "data"})
public class ApiResponse<T> {

    private String message;
    private Long count;
    private T data;
}