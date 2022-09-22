package com.example.english_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
public class Test {

    @Id
    @SequenceGenerator(sequenceName = "test_seq", name = "test_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_seq")
    private Long id;
    private String name;
    private String duration;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;

    public Test(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public void addQuestionToTest(Question question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
    }
}
