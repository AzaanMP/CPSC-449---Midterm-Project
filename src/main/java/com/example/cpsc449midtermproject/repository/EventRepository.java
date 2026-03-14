package com.example.cpsc449midtermproject.repository;

import com.example.cpsc449midtermproject.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Custom method to easily fetch only UPCOMING events for your GET /api/events endpoint
    List<Event> findByStatus(Event.EventStatus status);
}