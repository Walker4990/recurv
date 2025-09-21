package com.syc.recurv.domain.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    @Column(name = "resident_number")
    private String residentNumber;
    private String userIdStr;
    private String password;
    private String address;
    private String detailAddress;
    private String nickname;
    private String email;
    private String phone;
    private String role;
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
