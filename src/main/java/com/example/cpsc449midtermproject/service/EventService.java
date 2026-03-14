package com.example.cpsc449midtermproject.service;

import com.example.cpsc449midtermproject.entity.Event;
import com.example.cpsc449midtermproject.entity.Organizer;
import com.example.cpsc449midtermproject.entity.Venue;
import com.example.cpsc449midtermproject.exception.ResourceNotFoundException;
import com.example.cpsc449midtermproject.repository.EventRepository;
import com.example.cpsc449midtermproject.repository.OrganizerRepository;
import com.example.cpsc449midtermproject.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public Event createEvent(Event event, Long organizerId, Long venueId) {
        Organizer organizer = organizerRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with ID: " + organizerId));

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + venueId));

        event.setOrganizer(organizer);
        event.setVenue(venue);
        return eventRepository.save(event);
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByStatus(Event.EventStatus.UPCOMING);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + eventId));
    }
}