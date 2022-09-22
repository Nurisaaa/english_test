package com.example.english_test.api;

import com.example.english_test.dto.request.TestRequest;
import com.example.english_test.dto.response.TestInnerPageResponse;
import com.example.english_test.dto.response.TestResponse;
import com.example.english_test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/test")
@RestController
public class TestApi {

    private final TestService testService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public TestResponse save(@RequestBody TestRequest testRequest){
       return testService.save(testRequest);
    }

    @GetMapping("/{id}")
    public TestInnerPageResponse getById(@PathVariable Long id){
        return testService.getTestById(id);
    }




}

