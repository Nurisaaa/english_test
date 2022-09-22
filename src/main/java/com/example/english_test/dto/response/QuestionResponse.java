package com.example.english_test.dto.response;

import com.example.english_test.model.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private Long id;
    private String question;
    private QuestionType questionType;
    private List<OptionResponse> options;

    public QuestionResponse(Long id, String question, QuestionType questionType) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
    }
}
