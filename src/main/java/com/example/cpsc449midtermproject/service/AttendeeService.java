package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.dto.AttendeeBookingsDTO;
import com.example.cpsc449midtermproject.dto.BookingResponseDTO;
import com.example.cpsc449midtermproject.entity.Attendee;
import com.example.cpsc449midtermproject.entity.Booking;
import com.example.cpsc449midtermproject.exception.ResourceNotFoundException;
import com.example.cpsc449midtermproject.repository.AttendeeRepository;
import com.example.cpsc449midtermproject.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final BookingRepository bookingRepository; // Added to fetch bookings

    @Transactional
    public Attendee createAttendee(Attendee attendee) {
        // Enforce the unique email rule
        if (attendeeRepository.existsByEmail(attendee.getEmail())) {
            throw new IllegalArgumentException("An attendee with this email already exists.");
        }
        return attendeeRepository.save(attendee);
    }

    // NEW: Handles the business logic and DTO mapping for the controller
    public AttendeeBookingsDTO getBookingsForAttendee(Long attendeeId) {
        // 1. Verify the attendee exists
        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendee not found with ID: " + attendeeId));

        // 2. Fetch all their bookings
        List<Booking> bookings = bookingRepository.findByAttendee_AttendeeId(attendeeId);

        // 3. Map the Booking entities to BookingResponseDTOs
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

        // 4. Wrap everything in the final AttendeeBookingsDTO
        AttendeeBookingsDTO response = new AttendeeBookingsDTO();
        response.setAttendeeName(attendee.getName());
        response.setBookings(bookingDTOs);

        return response;
    }
}