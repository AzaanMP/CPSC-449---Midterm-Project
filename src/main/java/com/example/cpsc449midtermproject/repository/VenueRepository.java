package com.example.cpsc449midtermproject.repository;

import com.example.cpsc449midtermproject.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
}