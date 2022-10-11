package com.example.english_test.service;

import com.example.english_test.dto.request.AuthRequest;
import com.example.english_test.dto.request.StudentRegisterRequest;
import com.example.english_test.dto.response.AuthResponse;
import com.example.english_test.dto.response.SimpleResponse;
import com.example.english_test.dto.response.StudentRegisterResponse;
import com.example.english_test.model.AuthInfo;
import com.example.english_test.model.Student;
import com.example.english_test.model.enums.Role;
import com.example.english_test.repostitory.AuthInfoRepository;
import com.example.english_test.repostitory.StudentRepository;
import com.example.english_test.security.jwt.JwtUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {

    private final AuthInfoRepository authInfoRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    @PostConstruct
    public void init() throws IOException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource("english_test.json").getInputStream());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp.initializeApp(options);
    }

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

    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        AuthInfo authInfo = authInfoRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("user not found"));
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject("[english_test] reset password link");
        helper.setFrom("english_test@gmail.com");
        helper.setTo(email);
        helper.setText(link + "/" + authInfo.getId(), true);
        mailSender.send(mimeMessage);
        return new SimpleResponse("email send");
    }

    public SimpleResponse resetPassword(Long id, String newPassword) {
        AuthInfo authInfo = authInfoRepository.getById(id);
        authInfo.setPassword(passwordEncoder.encode(newPassword));
        return new SimpleResponse("password updated");
    }

    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
        Student student;
        if (!authInfoRepository.existsAuthInfoByEmail(firebaseToken.getEmail())) {
            Student newStudent = new Student();
            newStudent.setFirstName(firebaseToken.getName());
            newStudent.setAuthInfo(new AuthInfo(firebaseToken.getEmail(), firebaseToken.getEmail(), Role.STUDENT));
            student = studentRepository.save(newStudent);
        }
        student = studentRepository.findStudentByAuthInfoEmail(firebaseToken.getEmail());
        String token = jwtUtils.generateToken(student.getAuthInfo().getEmail());
        return new AuthResponse(student.getAuthInfo().getEmail(),
                token,
                student.getAuthInfo().getRole());
    }
}

