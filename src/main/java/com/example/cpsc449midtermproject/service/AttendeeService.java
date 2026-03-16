package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.dto.AttendeeBookingsDTO;
import com.example.cpsc449midtermproject.dto.BookingResponseDTO;
import com.example.cpsc449midtermproject.entity.Attendee;
import com.example.cpsc449midtermproject.entity.Booking;
import com.example.cpsc449midtermproject.entity.Event;
import com.example.cpsc449midtermproject.entity.TicketType;
import com.example.cpsc449midtermproject.repository.AttendeeRepository;
import com.example.cpsc449midtermproject.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Attendee createAttendee(Attendee attendee) {
        // Enforce the unique email rule
        if (attendeeRepository.existsByEmail(attendee.getEmail())) {
            throw new IllegalArgumentException("An attendee with this email already exists.");
        }
        return attendeeRepository.save(attendee);
    }

    @Transactional(readOnly = true)
    public AttendeeBookingsDTO getBookingsForAttendee(Long attendeeId) {

        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new RuntimeException("Attendee not found"));

        List<Booking> bookings = bookingRepository.findByAttendee_AttendeeId(attendeeId);

        List<BookingResponseDTO> bookingDTOs = bookings.stream().map(booking -> {

            TicketType ticketType = booking.getTicketType();
            Event event = ticketType.getEvent();

            BookingResponseDTO dto = new BookingResponseDTO();

            dto.setBookingReference(booking.getBookingReference());
            dto.setBookingDate(booking.getBookingDate());
            dto.setPaymentStatus(booking.getPaymentStatus().name());
            dto.setAttendeeName(attendee.getName());
            dto.setEventTitle(event.getTitle());
            dto.setTicketTypeName(ticketType.getName());
            dto.setPrice(ticketType.getPrice());

            return dto;

        }).toList();

        return new AttendeeBookingsDTO(attendee.getName(), bookingDTOs);
    }
}