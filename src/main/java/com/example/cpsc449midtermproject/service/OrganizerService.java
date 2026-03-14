package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.entity.Organizer;
import com.example.cpsc449midtermproject.repository.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizerService {

    private final OrganizerRepository organizerRepository;

    @Transactional
    public Organizer createOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }
}