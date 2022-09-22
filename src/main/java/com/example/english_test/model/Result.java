package com.example.english_test.model;

import javax.persistence.*;

@Entity
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
}
