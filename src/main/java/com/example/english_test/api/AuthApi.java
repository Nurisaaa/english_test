package com.example.english_test.api;

import com.example.english_test.dto.request.AuthRequest;
import com.example.english_test.dto.request.StudentRegisterRequest;
import com.example.english_test.dto.response.AuthResponse;
import com.example.english_test.dto.response.SimpleResponse;
import com.example.english_test.dto.response.StudentRegisterResponse;
import com.example.english_test.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest){
        return authService.login(authRequest);
    }

    @PostMapping
    public StudentRegisterResponse register(@RequestBody StudentRegisterRequest request){
        return authService.register(request);
    }

    @GetMapping("/forgot/password")
    public SimpleResponse forgotPassword(@RequestParam String email,@RequestParam String link) throws MessagingException {
        return authService.forgotPassword(email,link);
    }

    @PostMapping("/resetPassword/{id}")
    public SimpleResponse resetPassword(@PathVariable Long id,String newPassword){
       return authService.resetPassword(id,newPassword);
    }

    @PostMapping("/auth/google")
    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        return authService.authWithGoogle(tokenId);
    }
}
