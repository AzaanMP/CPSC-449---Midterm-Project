package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.dto.BookTicketRequestDTO;
import com.example.cpsc449midtermproject.dto.BookingResponseDTO;
import com.example.cpsc449midtermproject.entity.Booking;
import com.example.cpsc449midtermproject.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // POST /api/bookings -> Book a ticket
    @PostMapping
    public ResponseEntity<BookingResponseDTO> bookTicket(@RequestBody BookTicketRequestDTO request) {
        Booking booking = bookingService.bookTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToBookingResponseDTO(booking));
    }

    // PUT /api/bookings/{id}/cancel -> Cancel a booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        Booking cancelledBooking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(mapToBookingResponseDTO(cancelledBooking));
    }

    // --- Helper Method ---
    private BookingResponseDTO mapToBookingResponseDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingReference(booking.getBookingReference());
        dto.setBookingDate(booking.getBookingDate());
        dto.setPaymentStatus(booking.getPaymentStatus().name());
        dto.setAttendeeName(booking.getAttendee().getName());
        dto.setEventTitle(booking.getTicketType().getEvent().getTitle());
        dto.setTicketTypeName(booking.getTicketType().getName());
        dto.setPrice(booking.getTicketType().getPrice());
        return dto;
    }
}