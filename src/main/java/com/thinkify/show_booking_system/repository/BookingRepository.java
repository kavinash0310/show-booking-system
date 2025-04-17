package com.thinkify.show_booking_system.repository;

import com.thinkify.show_booking_system.model.Booking;
import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByShow(Show show);
    Booking findByUserAndShowAndStatus(User user, Show show, String status);
}
