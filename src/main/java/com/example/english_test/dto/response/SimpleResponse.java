package com.example.english_test.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SimpleResponse {
    String message;

    public SimpleResponse(String message) {
        this.message = message;
    }
}
