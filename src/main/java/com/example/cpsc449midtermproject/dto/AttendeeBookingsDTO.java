package com.example.cpsc449midtermproject.dto;

import lombok.Data;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeBookingsDTO {
    private String attendeeName;
    private List<BookingResponseDTO> bookings;
}