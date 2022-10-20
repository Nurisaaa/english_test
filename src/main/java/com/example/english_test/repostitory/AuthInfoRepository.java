package com.example.english_test.repostitory;

import com.example.english_test.model.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    Optional<AuthInfo> findByEmail(String email);
    boolean existsAuthInfoByEmail(String email);
}