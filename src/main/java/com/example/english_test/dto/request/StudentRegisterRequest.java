package com.example.english_test.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentRegisterRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int age;
    private String email;
    private String password;
}
