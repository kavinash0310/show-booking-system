package com.thinkify.show_booking_system.repository;

import com.thinkify.show_booking_system.model.Genre;
import com.thinkify.show_booking_system.model.Show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByGenre(Genre genre);

    Optional<Show> findById(Long showId);
}
