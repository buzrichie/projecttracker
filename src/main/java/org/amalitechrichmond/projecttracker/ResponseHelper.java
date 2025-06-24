package org.amalitechrichmond.projecttracker;

import org.amalitechrichmond.projecttracker.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failure(String message, HttpStatus status) {
        return new ResponseEntity<>(new ApiResponse<>(false, message, null), status);
    }
}
