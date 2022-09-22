package com.example.english_test.dto.request;

import com.example.english_test.model.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionRequest {
    private String question;
    private QuestionType questionType;
    private List<OptionRequest> options;
}
