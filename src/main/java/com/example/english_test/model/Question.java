package com.example.english_test.model;

import com.example.english_test.model.enums.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "questions")
@NoArgsConstructor
public class Question {

    @Id
    @SequenceGenerator(sequenceName = "question_seq",name = "question_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "question_seq")
    private Long id;
    private String question;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Option> options;

    public Question(String question, QuestionType questionType) {
        this.question = question;
        this.questionType = questionType;
    }

    public void addOptionToQuestion(Option option){
        if (this.options == null) {
            this.options = new ArrayList<>();
        }
        this.options.add(option);
    }
}
