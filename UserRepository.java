package com.accountscheduling.accounts_scheduler.jwtAuth;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}