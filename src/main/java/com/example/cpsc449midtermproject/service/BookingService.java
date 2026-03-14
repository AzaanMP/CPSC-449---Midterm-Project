package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.dto.BookTicketRequestDTO;
import com.example.cpsc449midtermproject.entity.Attendee;
import com.example.cpsc449midtermproject.entity.Booking;
import com.example.cpsc449midtermproject.entity.TicketType;
import com.example.cpsc449midtermproject.exception.DuplicateBookingException;
import com.example.cpsc449midtermproject.exception.ResourceNotFoundException;
import com.example.cpsc449midtermproject.exception.TicketSoldOutException;
import com.example.cpsc449midtermproject.repository.AttendeeRepository;
import com.example.cpsc449midtermproject.repository.BookingRepository;
import com.example.cpsc449midtermproject.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final AttendeeRepository attendeeRepository;

    @Transactional
    public Booking bookTicket(BookTicketRequestDTO request) {
        // 1. Check that TicketType exists and has quantity > 0
        TicketType ticket = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket Type not found"));

        if (ticket.getQuantityAvailable() <= 0) {
            throw new TicketSoldOutException("Sorry, this ticket type is sold out.");
        }

        // 2. Check that attendee hasn't already booked this ticket type
        if (bookingRepository.existsByAttendee_AttendeeIdAndTicketType_TicketTypeId(
                request.getAttendeeId(), request.getTicketTypeId())) {
            throw new DuplicateBookingException("You have already booked this ticket type.");
        }

        Attendee attendee = attendeeRepository.findById(request.getAttendeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendee not found"));

        // 3. Decrement quantity
        ticket.setQuantityAvailable(ticket.getQuantityAvailable() - 1);

        // Build the Booking entity
        Booking booking = new Booking();
        booking.setAttendee(attendee);
        booking.setTicketType(ticket);
        booking.setBookingDate(LocalDateTime.now()); // 5. Set booking date
        booking.setPaymentStatus(Booking.PaymentStatus.CONFIRMED); // 6. Set payment status

        // Save initially to generate the auto-incremented booking ID
        booking.setBookingReference("TEMP");
        booking = bookingRepository.save(booking);

        // 4. Generate unique reference code: TKT-{year}-{zero-padded id}
        String year = String.valueOf(LocalDateTime.now().getYear());
        String paddedId = String.format("%05d", booking.getBookingId());
        booking.setBookingReference("TKT-" + year + "-" + paddedId);

        // Save again to lock in the final reference code
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        // 1. Verify booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getPaymentStatus() == Booking.PaymentStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled.");
        }

        // 2. Set status to CANCELLED
        booking.setPaymentStatus(Booking.PaymentStatus.CANCELLED);

        // 3. Increment ticket quantity back by 1
        TicketType ticket = booking.getTicketType();
        ticket.setQuantityAvailable(ticket.getQuantityAvailable() + 1);

        // 4. Everything saves automatically when the transaction completes
        return bookingRepository.save(booking);
    }

    // Custom Revenue Calculation
    public BigDecimal calculateEventRevenue(Long eventId) {
        return bookingRepository.calculateEventRevenue(eventId);
    }
}