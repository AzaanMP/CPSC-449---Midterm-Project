package com.example.cpsc449midtermproject.repository;

import com.example.cpsc449midtermproject.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // 1. Checks if an attendee already booked a specific ticket type (Requirement 4.2)
    boolean existsByAttendee_AttendeeIdAndTicketType_TicketTypeId(Long attendeeId, Long ticketTypeId);

    // 2. Fetches all bookings for a specific attendee (Requirement 4.1)
    List<Booking> findByAttendee_AttendeeId(Long attendeeId);

    // 3. The custom @Query required for the revenue calculation endpoint (Requirement 4.2)
    @Query("SELECT COALESCE(SUM(b.ticketType.price), 0) FROM Booking b " +
            "WHERE b.ticketType.event.eventId = :eventId AND b.paymentStatus = 'CONFIRMED'")
    BigDecimal calculateEventRevenue(@Param("eventId") Long eventId);
}