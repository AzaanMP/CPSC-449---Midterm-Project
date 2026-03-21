package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.entity.TicketType;
import com.example.cpsc449midtermproject.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeRepository ticketTypeRepository;

    @PostMapping
    public TicketType createTicketType(@RequestBody TicketType ticketType) {
        return ticketTypeRepository.save(ticketType);
    }
}