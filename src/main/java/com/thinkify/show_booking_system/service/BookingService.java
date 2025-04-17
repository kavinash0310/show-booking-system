package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.model.Booking;
import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.pojo.BookingResponse;
import com.thinkify.show_booking_system.pojo.ShowResponse;

import java.util.List;

public interface BookingService {
    Booking createBooking(String email, Long showId, Long bookingId, int numberOfTickets);
    BookingResponse mapToBookingResponse(Booking booking);
    Booking getBookingById(Long bookingId);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByUser(Long userId, String requesterEmail);
    List<Booking> getBookingsByShow(Long showId, String organizerEmail);
    BookingResponse cancelBooking(Long bookingId, String userEmail);
    Show getTrendingShow();
    ShowResponse mapToResponse(Show show);
}