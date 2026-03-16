package com.example.cpsc449midtermproject.controller;

import com.example.cpsc449midtermproject.dto.EventResponseDTO;
import com.example.cpsc449midtermproject.dto.RevenueDTO;
import com.example.cpsc449midtermproject.dto.TicketTypeDTO;
import com.example.cpsc449midtermproject.entity.Event;
import com.example.cpsc449midtermproject.service.BookingService;
import com.example.cpsc449midtermproject.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    // POST /api/events -> Create a new event
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody Map<String, Object> payload) {
        Long organizerId = Long.valueOf(payload.get("organizerId").toString());
        Long venueId = Long.valueOf(payload.get("venueId").toString());

        Event event = new Event();
        event.setTitle(payload.get("title").toString());
        event.setDescription(payload.getOrDefault("description", "").toString());
        event.setEventDate(java.time.LocalDateTime.parse(payload.get("eventDate").toString()));
        event.setStatus(Event.EventStatus.valueOf(payload.get("status").toString()));

        Event savedEvent = eventService.createEvent(event, organizerId, venueId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "eventId", savedEvent.getEventId(),
                "title", savedEvent.getTitle(),
                "message", "Event created successfully!"
        ));
    }

    // GET /api/events -> List all UPCOMING events
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents() {
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        List<EventResponseDTO> response = upcomingEvents.stream()
                .map(this::mapToEventResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/events/{id} -> Get event details with ticket types
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(mapToEventResponseDTO(event));
    }

    // GET /api/events/{id}/revenue -> Get total revenue for an event
    @GetMapping("/{id}/revenue")
    public ResponseEntity<RevenueDTO> getEventRevenue(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        BigDecimal revenue = bookingService.calculateEventRevenue(id);

        RevenueDTO dto = new RevenueDTO();
        dto.setEventTitle(event.getTitle());
        dto.setTotalRevenue(revenue);

        return ResponseEntity.ok(dto);
    }

    // --- Helper Method ---
    private EventResponseDTO mapToEventResponseDTO(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setEventId(event.getEventId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setStatus(event.getStatus().name());
        dto.setOrganizerName(event.getOrganizer().getName());
        dto.setVenueName(event.getVenue().getName());

        List<TicketTypeDTO> ticketDTOs = new ArrayList<>();
        dto.setTicketTypes(ticketDTOs);

        return dto;
    }
}