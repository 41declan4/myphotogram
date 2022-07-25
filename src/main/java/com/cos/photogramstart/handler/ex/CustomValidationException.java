package com.cos.photogramstart.handler.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

public class CustomValidationException extends RuntimeException {

    // 객체를 구분할 때 사용!!(JVM)
    private static final long serialVersionUID = 1L;

    @Getter
    private Map<String, String> errorMap;

    public CustomValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

}
