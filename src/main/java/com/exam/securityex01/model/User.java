package com.exam.securityex01.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

// ORM - Object Relation Mapping

@Data
@Entity
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN
    private String provider; // 예) google
    private String providerId; // google에서 사용되는 id -> sub에 해당함
    @CreationTimestamp
    private Timestamp createDate;
}