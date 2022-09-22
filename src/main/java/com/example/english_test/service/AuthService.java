package com.example.english_test.service;

import com.example.english_test.dto.request.AuthRequest;
import com.example.english_test.dto.request.StudentRegisterRequest;
import com.example.english_test.dto.response.AuthResponse;
import com.example.english_test.dto.response.StudentRegisterResponse;
import com.example.english_test.model.AuthInfo;
import com.example.english_test.model.Student;
import com.example.english_test.repostitory.AuthInfoRepository;
import com.example.english_test.repostitory.StudentRepository;
import com.example.english_test.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthInfoRepository authInfoRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        AuthInfo authInfo = authInfoRepository.findByEmail(authentication.getName()).
                orElseThrow(
                        () -> new BadCredentialsException("bad credentials")
                );

        String token = jwtUtils.generateToken(authInfo.getEmail());
        return new AuthResponse(authInfo.getUsername(), token, authInfo.getRole());
    }

    public StudentRegisterResponse register(StudentRegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Student student = new Student(request);
        Student savedStudent = studentRepository.save(student);
        String token = jwtUtils.generateToken(savedStudent.getAuthInfo().getEmail());
        return new StudentRegisterResponse(savedStudent.getFirstName(),
                savedStudent.getLastName(),
                savedStudent.getAuthInfo().getEmail(),
                token, savedStudent.getAuthInfo().getRole());
    }
}
