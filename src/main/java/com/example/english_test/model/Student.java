package com.example.english_test.model;

import com.example.english_test.dto.request.StudentRegisterRequest;
import com.example.english_test.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @SequenceGenerator(sequenceName = "student_seq", name = "student_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private AuthInfo authInfo;

    public Student(StudentRegisterRequest request) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.age = request.getAge();
        this.phoneNumber = request.getPhoneNumber();
        AuthInfo authInfo1 = new AuthInfo();
        authInfo1.setEmail(request.getEmail());
        authInfo1.setPassword(request.getPassword());
        authInfo1.setRole(Role.STUDENT);
        this.authInfo = authInfo1;
    }

    public Student(String firstName, String lastName, int age, String phoneNumber, AuthInfo authInfo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.authInfo = authInfo;
    }
}

