package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.entity.Venue;
import com.example.cpsc449midtermproject.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    @Transactional
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }
}