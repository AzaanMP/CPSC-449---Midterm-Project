package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.dto.AttendeeBookingsDTO;
import com.example.cpsc449midtermproject.dto.BookingResponseDTO;
import com.example.cpsc449midtermproject.entity.Attendee;
import com.example.cpsc449midtermproject.entity.Booking;
import com.example.cpsc449midtermproject.repository.AttendeeRepository;
import com.example.cpsc449midtermproject.repository.BookingRepository;
import com.example.cpsc449midtermproject.service.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;
    private final AttendeeRepository attendeeRepository;
    private final BookingRepository bookingRepository;

    // POST /api/attendees -> Return 201 Created
    @PostMapping
    public ResponseEntity<Map<String, Object>> registerAttendee(@RequestBody Attendee attendee) {
        Attendee savedAttendee = attendeeService.createAttendee(attendee);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "attendeeId", savedAttendee.getAttendeeId(),
                "name", savedAttendee.getName()
        ));
    }

    // GET /api/attendees/{id}/bookings -> Get all bookings for an attendee
    @GetMapping("/{id}/bookings")
    public ResponseEntity<AttendeeBookingsDTO> getAttendeeBookings(@PathVariable Long id) {
        Attendee attendee = attendeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendee not found"));

        List<Booking> bookings = bookingRepository.findByAttendee_AttendeeId(id);

        List<BookingResponseDTO> bookingDTOs = bookings.stream().map(booking -> {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setBookingReference(booking.getBookingReference());
            dto.setBookingDate(booking.getBookingDate());
            dto.setPaymentStatus(booking.getPaymentStatus().name());
            dto.setAttendeeName(attendee.getName());
            dto.setEventTitle(booking.getTicketType().getEvent().getTitle());
            dto.setTicketTypeName(booking.getTicketType().getName());
            dto.setPrice(booking.getTicketType().getPrice());
            return dto;
        }).collect(Collectors.toList());

        AttendeeBookingsDTO response = new AttendeeBookingsDTO();
        response.setAttendeeName(attendee.getName());
        response.setBookings(bookingDTOs);

        return ResponseEntity.ok(response);
    }
}