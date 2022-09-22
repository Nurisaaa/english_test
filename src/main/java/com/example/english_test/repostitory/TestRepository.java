package com.example.english_test.repostitory;

import com.example.english_test.dto.response.TestResponse;
import com.example.english_test.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestRepository extends JpaRepository<Test, Long> {
}