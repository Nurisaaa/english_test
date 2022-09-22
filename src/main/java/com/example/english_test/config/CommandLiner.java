//package com.example.english_test.config;
//
//import com.example.english_test.model.AuthInfo;
//import com.example.english_test.model.Student;
//import com.example.english_test.model.enums.Role;
//import com.example.english_test.repostitory.StudentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class CommandLiner {
//    private final PasswordEncoder passwordEncoder;
//    private final StudentRepository studentRepository;
//
//    @Bean
//    public CommandLineRunner commandLiner() {
//        return (args) -> {
//            if (studentRepository.findStudentByAuthInfoEmail("aijamal@gmail.com") == null) {
//                AuthInfo authInfo = new AuthInfo("aijamal@gmail.com", passwordEncoder.encode("aijamal"), Role.STUDENT);
//                Student student = new Student("Aijamal", "Asangazieva", 18, "0999123456", authInfo);
//                studentRepository.save(student);
//            }
//        };
//    }
//}
