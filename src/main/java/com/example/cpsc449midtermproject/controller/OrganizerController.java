package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.entity.Organizer;
import com.example.cpsc449midtermproject.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/organizers")
@RequiredArgsConstructor
public class OrganizerController {

    private final OrganizerService organizerService;

    // POST /api/organizers -> Create a new organizer
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrganizer(@RequestBody Organizer organizer) {
        Organizer savedOrganizer = organizerService.createOrganizer(organizer);

        // Returning a Map to avoid leaking the raw JPA Entity
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "organizerId", savedOrganizer.getOrganizerId(),
                "name", savedOrganizer.getName(),
                "email", savedOrganizer.getEmail(),
                "message", "Organizer created successfully!"
        ));
    }
}