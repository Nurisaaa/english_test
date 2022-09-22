package com.example.english_test.service;

import com.example.english_test.dto.request.OptionRequest;
import com.example.english_test.dto.request.QuestionRequest;
import com.example.english_test.dto.request.TestRequest;
import com.example.english_test.dto.response.OptionResponse;
import com.example.english_test.dto.response.QuestionResponse;
import com.example.english_test.dto.response.TestInnerPageResponse;
import com.example.english_test.dto.response.TestResponse;
import com.example.english_test.model.Option;
import com.example.english_test.model.Question;
import com.example.english_test.model.Test;
import com.example.english_test.repostitory.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public TestResponse save(TestRequest testRequest) {
        Test test = testRepository.save(convertToEntity(testRequest));
        return new TestResponse(test.getId(), test.getName(), test.getDuration());
    }

    public TestInnerPageResponse getTestById(Long id) {
        return convertToResponse(testRepository.findById(id).orElseThrow(
                () -> new RuntimeException("not found")
        ));
    }

    private TestInnerPageResponse convertToResponse(Test test) {
        TestInnerPageResponse testResponse = new TestInnerPageResponse(test.getId(), test.getName(), test.getDuration());
        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for (Question q : test.getQuestions()) {
            QuestionResponse question = new QuestionResponse(q.getId(), q.getQuestion(), q.getQuestionType());
            List<OptionResponse> optionResponses = new ArrayList<>();
            for (Option o : q.getOptions()) {
                OptionResponse option = new OptionResponse(o.getId(), o.getOption());
                optionResponses.add(option);
            }
            question.setOptions(optionResponses);
            questionResponseList.add(question);
        }
        testResponse.setQuestions(questionResponseList);
        return testResponse;
    }

    private Test convertToEntity(TestRequest testRequest) {
        Test test = new Test(testRequest.getName(), testRequest.getDuration());
        for (QuestionRequest q : testRequest.getQuestions()) {
            Question question = new Question(q.getQuestion(), q.getQuestionType());

            for (OptionRequest o : q.getOptions()) {
                Option option = new Option(o.getOption(), o.getIsTrue());
                question.addOptionToQuestion(option);
            }
            test.addQuestionToTest(question);
        }
        return test;
    }
}
