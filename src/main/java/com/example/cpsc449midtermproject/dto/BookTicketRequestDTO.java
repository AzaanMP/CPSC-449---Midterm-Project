package com.example.cpsc449midtermproject.dto;

import lombok.Data;

@Data
public class BookTicketRequestDTO {
    private Long attendeeId;
    private Long ticketTypeId;
}