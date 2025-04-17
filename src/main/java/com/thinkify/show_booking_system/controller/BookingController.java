package com.thinkify.show_booking_system.controller;

import com.thinkify.show_booking_system.exception.ShowNotFoundException;
import com.thinkify.show_booking_system.model.Booking;
import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.pojo.BookingResponse;
import com.thinkify.show_booking_system.pojo.ShowResponse;
import com.thinkify.show_booking_system.security.JwtUtil;
import com.thinkify.show_booking_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BookingController(BookingService bookingService, JwtUtil jwtUtil) {
        this.bookingService = bookingService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestParam String email, @RequestParam Long showId,@RequestParam(required = false) Long bookingId,
                                                         @RequestParam int numberOfTickets) {
        Booking booking = bookingService.createBooking(email, showId, bookingId, numberOfTickets);
        BookingResponse response = bookingService.mapToBookingResponse(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        BookingResponse response = bookingService.mapToBookingResponse(booking);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userId, @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractToken(authHeader);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId, email));
    }

    @GetMapping("/show/{showId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<List<Booking>> getBookingsByShow(@PathVariable Long showId,@RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractToken(authHeader);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(bookingService.getBookingsByShow(showId, email));
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId, @RequestParam String userEmail) {
        BookingResponse response = bookingService.cancelBooking(bookingId, userEmail);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/trending-show")
    public ResponseEntity<ShowResponse> getTrendingShow() {
        try {
            Show trendingShow = bookingService.getTrendingShow();
            ShowResponse response = bookingService.mapToResponse(trendingShow);
            return ResponseEntity.ok(response);
        } catch (ShowNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ShowResponse());
        }
    }
}