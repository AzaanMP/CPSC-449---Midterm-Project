package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.entity.Venue;
import com.example.cpsc449midtermproject.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    // POST /api/venues -> Create a new venue
    @PostMapping
    public ResponseEntity<Map<String, Object>> createVenue(@RequestBody Venue venue) {
        Venue savedVenue = venueService.createVenue(venue);

        // Returning a Map to avoid leaking the raw JPA Entity
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "venueId", savedVenue.getVenueId(),
                "name", savedVenue.getName(),
                "city", savedVenue.getCity(),
                "message", "Venue created successfully!"
        ));
    }
}