package com.example.english_test.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestRequest {
    private String name;
    private String duration;
    private List<QuestionRequest> questions;
}
