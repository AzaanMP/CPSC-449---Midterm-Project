package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.entity.Attendee;
import com.example.cpsc449midtermproject.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    @Transactional
    public Attendee createAttendee(Attendee attendee) {
        // Enforce the unique email rule
        if (attendeeRepository.existsByEmail(attendee.getEmail())) {
            throw new IllegalArgumentException("An attendee with this email already exists.");
        }
        return attendeeRepository.save(attendee);
    }
}