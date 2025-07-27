package com.exam.securityex01.repository;

import com.exam.securityex01.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IoC 가능
// Ioc: 사용자 흐름으로 제어되는 것이 아니라 프로그램 자체가 제어권을 가진다

public interface UserRepository extends JpaRepository<User, Integer> {

}
