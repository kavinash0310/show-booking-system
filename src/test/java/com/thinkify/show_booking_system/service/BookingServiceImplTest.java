package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.model.*;
import com.thinkify.show_booking_system.repository.BookingRepository;
import com.thinkify.show_booking_system.repository.ShowRepository;
import com.thinkify.show_booking_system.repository.WaitListRepository;
import com.thinkify.show_booking_system.service.Impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.thinkify.show_booking_system.constant.Constants.STATUS_BOOKED;
import static com.thinkify.show_booking_system.constant.Constants.STATUS_CANCELLED;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private UserService userService;

    @Mock
    private ShowService showService;

    @Mock
    private WaitListRepository waitListRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setNumberOfTickets(2);
        booking.setStatus(STATUS_BOOKED);
    }

    @Test
    void createBooking() {
        String email = "user@example.com";
        Long showId = 1L;
        Long bookingId = 1L;
        int numberOfTickets = 2;

        when(userService.getUserByEmail(email)).thenReturn(new User());
        when(showService.getShowById(showId)).thenReturn(new Show());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(email, showId, bookingId, numberOfTickets);

        assertNotNull(createdBooking);
        assertEquals(1L, createdBooking.getBookingId());
        assertEquals(2, createdBooking.getNumberOfTickets());
    }

    @Test
    void getBookingById() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking fetchedBooking = bookingService.getBookingById(bookingId);

        assertNotNull(fetchedBooking);
        assertEquals(1L, fetchedBooking.getBookingId());
    }

    @Test
    void getBookingsByUser() {
        Long userId = 1L;
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userService.getUserById(userId)).thenReturn(user);
        when(bookingRepository.findByUser(user)).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getBookingsByUser(userId, email);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
    }

    @Test
    void getTrendingShow() {
        Show trendingShow = new Show();
        trendingShow.setId(1L);
        trendingShow.setTitle("Trending Show");
        trendingShow.setSlots(new ArrayList<>());
        trendingShow.setOrganizer(new User());

        Booking booking = new Booking();
        booking.setShow(trendingShow);
        booking.setNumberOfTickets(10);

        when(showService.getAllShows()).thenReturn(List.of(trendingShow));
        when(bookingRepository.findByShow(trendingShow)).thenReturn(List.of(booking));

        Show result = bookingService.getTrendingShow();

        assertNotNull(result);
        assertEquals("Trending Show", result.getTitle());
    }
}