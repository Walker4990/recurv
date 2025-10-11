package com.syc.recurv.domain.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    @JsonIgnore // 주민등록번호는 응답에 안나감.
    @Column(name = "resident_number")
    private String residentNumber;
    @Column(name = "user_id_str", unique = true, nullable = false)
    private String userIdStr;
    private String password;
    private String address;
    private String detailAddress;
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    private String phone;
    private String role;
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
    private String status="ACTIVE";

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
