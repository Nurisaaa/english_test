package com.example.english_test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestResponse {
    private Long id;
    private String name;
    private String duration;
}
