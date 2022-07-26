package com.cos.photogramstart.handler.ex;

import lombok.Getter;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException {

    // 객체를 구분할 때 사용!!(JVM)
    private static final long serialVersionUID = 1L;

    @Getter
    private Map<String, String> errorMap;

    public CustomValidationApiException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public CustomValidationApiException(String message) {
        super(message);
    }


}
