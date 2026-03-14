package com.example.cpsc449midtermproject.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketTypeDTO {
    private Long ticketTypeId;
    private String name;
    private BigDecimal price;
    private Integer quantityAvailable;
}