/**
 * User model 생성
 */

package com.example.password_practice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

 /**
 * '@Entity' 가 붙은 클래스 이름은 특별히 지정하지 않으면 기본적으로 데이터베이스의 테이블 이름이 됩니다.
 * 예를 들어 User 클래스에 '@Entity' 를 붙이면 user 또는 user_table과 같은 이름의 테이블과 연결됩니다.
 **/
@Entity
@Table(name = "users")  // users table과 같은 이름의 테이블과 연결
public class User {

    @Id     // 이 클래스를 엔티티로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략
    private Long id;

    @Column(unique = true, nullable = false)  // 컬럼의 속성 지정 (널 허용 안함, 유일성 보장)
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자명은 3-20자여야 합니다")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "패스워드는 필수입니다")
    private String password; // 해시된 패스워드가 저장됨

    @Column(unique = true, nullable = false)
    @Email(message = "올바른 이메일 형식이어야 합니다")   //  @Email은 이메일 형식의 유효성을 검사하는 데 사용되는 어노테이션입니다
    private String email;

    @Column(nullable = false)
    private Boolean enabled = true;

     // 자바 필드명은 'createdAt'이지만, 데이터베이스 컬럼명은 'created_at'으로 매핑
    @Column(name = "created_at")
    private LocalDateTime createdAt;        // 생성 시간을 기록할 필드

     // 자바 필드명은 'passwordChangedAt'
     // 데이터베이스 컬럼명은 'password_changed_at'
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    // 기본 생성자
    protected User() {}

    // 생성자
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.passwordChangedAt = LocalDateTime.now();
    }

    // Getter, Setter
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Boolean getEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }

    public void setUsername(String username) { this.username = username; }

    // 패스워드 설정 시 변경 시간 업데이트
    public void setPassword(String password) {
        this.password = password;
        this.passwordChangedAt = LocalDateTime.now();
    }

    public void setEmail(String email) { this.email = email; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    // @Override를 사용하여 toString() 메서드를 재정의합니다.
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                '}';
    }
}