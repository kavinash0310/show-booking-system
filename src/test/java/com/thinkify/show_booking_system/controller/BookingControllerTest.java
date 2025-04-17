package com.thinkify.show_booking_system.controller;

import com.thinkify.show_booking_system.model.Booking;
import com.thinkify.show_booking_system.model.Genre;
import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.pojo.BookingResponse;
import com.thinkify.show_booking_system.pojo.ShowResponse;
import com.thinkify.show_booking_system.security.JwtUtil;
import com.thinkify.show_booking_system.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BookingController bookingController;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setNumberOfTickets(2);
    }

    @Test
    void createBooking() {
        String email = "user@example.com";
        Long showId = 1L;
        Long bookingId = 1L;
        int numberOfTickets = 2;

        when(bookingService.createBooking(email, showId, bookingId, numberOfTickets)).thenReturn(booking);
        when(bookingService.mapToBookingResponse(booking)).thenReturn(new BookingResponse(1L, email, null, 2, "BOOKED", null));

        ResponseEntity<BookingResponse> response = bookingController.createBooking(email, showId, bookingId, numberOfTickets);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getBookingId());
        assertEquals("user@example.com", response.getBody().getUserEmail());
    }

    @Test
    void getBookingById() {
        Long bookingId = 1L;
        when(bookingService.getBookingById(bookingId)).thenReturn(booking);
        when(bookingService.mapToBookingResponse(booking)).thenReturn(new BookingResponse(1L, "user@example.com", null, 2, "BOOKED", null));

        ResponseEntity<BookingResponse> response = bookingController.getBookingById(bookingId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getBookingId());
    }

    @Test
    void getAllBookings() {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void cancelBooking() {
        Long bookingId = 1L;
        String userEmail = "user@example.com";

        when(bookingService.cancelBooking(bookingId, userEmail)).thenReturn(new BookingResponse(1L, userEmail, null, 2, "CANCELLED", null));

        ResponseEntity<BookingResponse> response = bookingController.cancelBooking(bookingId, userEmail);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("CANCELLED", response.getBody().getStatus());
    }

    @Test
    void getTrendingShow() {
        Show show = new Show();
        show.setId(1L);
        show.setTitle("Trending Show");

        when(bookingService.getTrendingShow()).thenReturn(show);
        when(bookingService.mapToResponse(show)).thenReturn(new ShowResponse(1L, "Trending Show", Genre.COMEDY, "Description", null, null));

        ResponseEntity<ShowResponse> response = bookingController.getTrendingShow();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Trending Show", response.getBody().getTitle());
    }
}
