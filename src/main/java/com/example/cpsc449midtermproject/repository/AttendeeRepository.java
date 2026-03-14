package com.example.cpsc449midtermproject.repository;

import com.example.cpsc449midtermproject.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    // Custom method to help validate unique emails when registering
    boolean existsByEmail(String email);
}