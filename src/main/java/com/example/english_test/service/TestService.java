package com.example.english_test.service;

import com.example.english_test.dto.request.OptionRequest;
import com.example.english_test.dto.request.PassTestRequest;
import com.example.english_test.dto.request.QuestionRequest;
import com.example.english_test.dto.request.TestRequest;
import com.example.english_test.dto.response.*;
import com.example.english_test.model.*;
import com.example.english_test.repostitory.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ResultRepository resultRepository;

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

    public ResultResponse passTest(PassTestRequest passTestRequest, AuthInfo authInfo) {
        Integer countOfCorrectAnswer = 0;
        Test test = testRepository.findById(passTestRequest.getTestId()).
                orElseThrow(() -> new RuntimeException("test not found"));
        for (Map.Entry<Long, List<Long>> answer : passTestRequest.getAnswers().entrySet()) {
            for (Long optionId : answer.getValue()) {
                Option option = optionRepository.findById(optionId).get();
                if (option.getIsTrue()) {
                    countOfCorrectAnswer++;
                }
            }
        }
        Student student1 = studentRepository.findStudentByAuthInfoEmail(authInfo.getEmail());
        Result result = new Result(countOfCorrectAnswer,test.getQuestions().size() + 1 - countOfCorrectAnswer,countOfCorrectAnswer,student1);
        resultRepository.save(result);
        return new ResultResponse(student1.getFirstName(),
                countOfCorrectAnswer,
                test.getQuestions().size() - countOfCorrectAnswer, countOfCorrectAnswer);
    }
}
