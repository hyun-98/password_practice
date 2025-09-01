package com.example.password_practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 패스워드 암호화 동작을 테스트하는 컨트롤러
 * 실제 운영에서는 제거해야 함
 */
@RestController
@RequestMapping("/password-test")
public class PasswordTestController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordTestController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 패스워드 테스트 폼 페이지
     */
    @GetMapping("/form")
    public String passwordTestForm() {
        return "password-test";
    }

    /**
     * 패스워드 해싱 테스트
     */
    @PostMapping("/encode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> encodePassword(@RequestParam String password) {
        long startTime = System.currentTimeMillis();

        String encoded = passwordEncoder.encode(password);

        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("original", password);
        response.put("encoded", encoded);
        response.put("length", encoded.length());
        response.put("encodingTime", endTime - startTime);

        // BCrypt 구조 분석
        if (encoded.startsWith("$2b$")) {
            String[] parts = encoded.split("\\$");
            response.put("algorithm", parts[1]);
            response.put("cost", parts[2]);
            response.put("salt", parts[3].substring(0, 22));
            response.put("hash", parts[3].substring(22));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 패스워드 검증 테스트
     */
    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyPassword(@RequestParam String password,
                                                              @RequestParam String encoded) {
        long startTime = System.currentTimeMillis();

        boolean matches = passwordEncoder.matches(password, encoded);

        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("password", password);
        response.put("encoded", encoded);
        response.put("matches", matches);
        response.put("verificationTime", endTime - startTime);

        return ResponseEntity.ok(response);
    }

    /**
     * 같은 패스워드의 다른 해시값 생성 테스트
     */
    @PostMapping("/multiple-encode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> multipleEncode(@RequestParam String password,
                                                              @RequestParam(defaultValue = "3") int count) {
        Map<String, Object> response = new HashMap<>();
        response.put("original", password);

        Map<Integer, String> results = new HashMap<>();

        for (int i = 1; i <= count; i++) {
            String encoded = passwordEncoder.encode(password);
            results.put(i, encoded);
        }

        response.put("encodedVersions", results);
        response.put("note", "같은 패스워드라도 Salt 때문에 매번 다른 해시값이 생성됩니다");

        return ResponseEntity.ok(response);
    }
}