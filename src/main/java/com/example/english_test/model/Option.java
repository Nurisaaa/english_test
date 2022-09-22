package com.example.english_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
public class Option {

    @Id
    @SequenceGenerator(sequenceName = "option_seq",name = "option_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "option_seq")
    private Long id;
    private String option;
    private Boolean isTrue;

    public Option(String option, Boolean isTrue) {
        this.option = option;
        this.isTrue = isTrue;
    }
}
