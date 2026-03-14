package com.example.cpsc449midtermproject.dto;

import lombok.Data;
import java.util.List;

@Data
public class AttendeeBookingsDTO {
    private String attendeeName;
    private List<BookingResponseDTO> bookings;
}