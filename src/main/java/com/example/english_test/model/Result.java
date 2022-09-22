package com.example.english_test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "results")
public class Result {
    @Id
    @SequenceGenerator(sequenceName = "question_seq",name = "question_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "question_seq")
    private Long id;
    private Integer amountOfCorrectAnswers;
    private Integer amountOfWrongAnswers;
    private Integer point;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH})
    private Student student;

    public Result(Integer amountOfCorrectAnswers, Integer amountOfWrongAnswers, Integer point, Student student) {
        this.amountOfCorrectAnswers = amountOfCorrectAnswers;
        this.amountOfWrongAnswers = amountOfWrongAnswers;
        this.point = point;
        this.student = student;
    }
}
