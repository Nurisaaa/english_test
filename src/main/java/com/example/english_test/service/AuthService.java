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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthInfoRepository authInfoRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @PostConstruct
    void init() throws IOException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource("english_test.json").getInputStream());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
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

    @Async
    public SimpleResponse forgotPassword(String to, String link) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject("[english_test] reset password link");
            helper.setFrom("english_test@gmail.com");
            helper.setTo(to);
            helper.setText(link, true);
            mailSender.send(mimeMessage);
            return new SimpleResponse("Mailing sent success");
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);

        Student student = null;
        if (!authInfoRepository.existsAuthInfoByEmail(firebaseToken.getEmail())) {
            Student newStudent = new Student();
            newStudent.setAuthInfo(new AuthInfo(firebaseToken.getEmail(),
                    firebaseToken.getEmail(),
                    Role.STUDENT));
            student = studentRepository.save(newStudent);
        } else {
            student = studentRepository.findStudentByAuthInfoEmail(firebaseToken.getEmail());

        }
        String token = jwtUtils.generateToken(student.getAuthInfo().getEmail());
        return new AuthResponse(
                student.getAuthInfo().getEmail(), token, student.getAuthInfo().getRole());
    }
}
