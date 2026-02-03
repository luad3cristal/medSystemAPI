package com.emailservice.EmailService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emailservice.EmailService.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

}
