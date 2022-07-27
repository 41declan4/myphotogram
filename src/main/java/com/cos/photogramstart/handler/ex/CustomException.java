package com.cos.photogramstart.handler.ex;

import lombok.Getter;

import java.util.Map;

public class CustomException extends RuntimeException {

    // 객체를 구분할 때 사용!!(JVM)
    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
        super(message);
    }

}
