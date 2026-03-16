package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.dto.AttendeeBookingsDTO;
import com.example.cpsc449midtermproject.entity.Attendee;
import com.example.cpsc449midtermproject.service.AttendeeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendees")
public class AttendeeController {

    private final AttendeeService attendeeService;

    public AttendeeController(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }

    // Register a new attendee
    @PostMapping
    public ResponseEntity<Attendee> createAttendee(@RequestBody Attendee attendee) {

        Attendee savedAttendee = attendeeService.createAttendee(attendee);

        return new ResponseEntity<>(savedAttendee, HttpStatus.CREATED);
    }

    // Get all bookings for an attendee
    @GetMapping("/{id}/bookings")
    public ResponseEntity<AttendeeBookingsDTO> getAttendeeBookings(@PathVariable Long id) {

        AttendeeBookingsDTO attendeeBookings = attendeeService.getBookingsForAttendee(id);

        return ResponseEntity.ok(attendeeBookings);
    }
}
