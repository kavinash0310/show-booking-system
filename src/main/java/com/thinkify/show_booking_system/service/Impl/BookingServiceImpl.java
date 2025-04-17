package com.thinkify.show_booking_system.service.Impl;

import com.thinkify.show_booking_system.exception.BookingNotFoundException;
import com.thinkify.show_booking_system.exception.ResourceNotFoundException;
import com.thinkify.show_booking_system.exception.ShowNotFoundException;
import com.thinkify.show_booking_system.model.*;
import com.thinkify.show_booking_system.pojo.BookingResponse;
import com.thinkify.show_booking_system.pojo.OrganizerResponse;
import com.thinkify.show_booking_system.pojo.ShowResponse;
import com.thinkify.show_booking_system.pojo.SlotResponse;
import com.thinkify.show_booking_system.repository.BookingRepository;
import com.thinkify.show_booking_system.repository.WaitListRepository;
import com.thinkify.show_booking_system.service.BookingService;
import com.thinkify.show_booking_system.service.ShowService;
import com.thinkify.show_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.thinkify.show_booking_system.constant.Constants.*;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ShowService showService;
    private final WaitListRepository waitListRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ShowService showService, WaitListRepository waitListRepository) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.showService = showService;
        this.waitListRepository = waitListRepository;
    }

    public Booking createBooking(String email, Long showId, Long bookingId, int numberOfTickets) {
        User user = userService.getUserByEmail(email);
        if (user == null) throw new ResourceNotFoundException(USER_NOT_FOUND  + email);
        Show show = showService.getShowById(showId);
        if (show == null) throw new ResourceNotFoundException(SHOW_NOT_FOUND  + showId);

        List<Booking> userBookings = bookingRepository.findByUser(user);
        for (Slot slot : show.getSlots()) {
            boolean alreadyBookedInSameSlot = userBookings.stream().anyMatch(b ->
                    b.getSlot() != null && isOverlapping(b.getSlot(), slot));

            if (!alreadyBookedInSameSlot && slot.getCapacity() - slot.getBookedSeats() >= numberOfTickets) {
                slot.setBookedSeats(slot.getBookedSeats() + numberOfTickets);

                Booking booking = new Booking();
                booking.setBookingId(bookingId);
                booking.setUser(user);
                booking.setShow(show);
                booking.setSlot(slot);
                booking.setNumberOfTickets(numberOfTickets);
                booking.setStatus(STATUS_BOOKED);

                Show trendingShow = getTrendingShow();
                ShowResponse trendingShowResponse = mapToResponse(trendingShow);
                System.out.println("Trending Show " + trendingShow.getTitle());

                return bookingRepository.save(booking);
            }
        }

        Waitlist waitlist = new Waitlist();
        waitlist.setUser(user);
        waitlist.setShow(show);
        waitlist.setNumberOfTickets(numberOfTickets);

        Slot firstSlot = show.getSlots().isEmpty() ? null : show.getSlots().get(0);
        waitlist.setSlot(firstSlot);

        waitListRepository.save(waitlist);

        Booking waitlistedBooking = new Booking();
        waitlistedBooking.setBookingId(bookingId);
        waitlistedBooking.setUser(user);
        waitlistedBooking.setShow(show);
        waitlistedBooking.setSlot(firstSlot);
        waitlistedBooking.setNumberOfTickets(numberOfTickets);
        waitlistedBooking.setStatus(STATUS_WAITLISTED);

        return bookingRepository.save(waitlistedBooking);
    }


    private boolean isOverlapping(Slot slot1, Slot slot2) {
        return slot1.getStartTime().isBefore(slot2.getEndTime()) &&
                slot2.getStartTime().isBefore(slot1.getEndTime());
    }

    public BookingResponse mapToBookingResponse(Booking booking) {
        Slot slot = booking.getSlot();

        SlotResponse slotInfo = null;
        if (slot != null) {
            int availableSeats = slot.getCapacity() - slot.getBookedSeats();
            slotInfo = new SlotResponse(slot.getStartTime(), slot.getEndTime(), slot.getCapacity(), slot.getBookedSeats(), availableSeats);
        }
        OrganizerResponse organizerResponse = new OrganizerResponse(booking.getShow().getOrganizer().getEmail());

        return new BookingResponse(
                booking.getBookingId(),
                booking.getUser().getEmail(),
                organizerResponse,
                booking.getNumberOfTickets(),
                booking.getStatus(),
                slotInfo
        );
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(BOOKING_NOT_FOUND + bookingId));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUser(Long userId, String requesterEmail) {
        User user = userService.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException(USER_NOT_FOUND);
        if (!user.getEmail().equals(requesterEmail)) {
            throw new AccessDeniedException("You can only view your own booking");
        }
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getBookingsByShow(Long showId, String organizerEmail) {
        Show show = showService.getShowById(showId);
        if (show == null) throw new ResourceNotFoundException(SHOW_NOT_FOUND);
        if (!show.getOrganizer().getEmail().equals(organizerEmail)) {
            throw new AccessDeniedException(NOT_YOUR_BOOKING);
        }
        return bookingRepository.findByShow(show);
    }

    public BookingResponse cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(BOOKING_NOT_FOUND+ bookingId));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException(NOT_YOUR_BOOKING);
        }
        if (STATUS_CANCELLED.equals(booking.getStatus())) {
            throw new IllegalStateException(ALREADY_CANCELLED);
        }

        Slot slot = booking.getSlot();
        if (slot != null) {
            int updatedBookedSeats = slot.getBookedSeats() - booking.getNumberOfTickets();
            slot.setBookedSeats(Math.max(updatedBookedSeats, 0));
        }

        booking.setStatus(STATUS_CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);

        Show trendingShow = getTrendingShow();
        ShowResponse trendingShowResponse = mapToResponse(trendingShow);
        System.out.println("Trending Show: " + trendingShow.getTitle());

        List<Waitlist> waitlistEntries = waitListRepository.findBySlot(slot);
        if (!waitlistEntries.isEmpty()) {
            Waitlist firstWaitlistedUser = waitlistEntries.get(0);
            Booking waitlistedBooking = bookingRepository.findByUserAndShowAndStatus(
                    firstWaitlistedUser.getUser(),
                    booking.getShow(),
                    STATUS_WAITLISTED
            );
            if (waitlistedBooking != null) {
                waitlistedBooking.setStatus(STATUS_BOOKED);
                waitlistedBooking.setSlot(slot);
                slot.setBookedSeats(slot.getBookedSeats() + waitlistedBooking.getNumberOfTickets());
                bookingRepository.save(waitlistedBooking);
            } else {
                createBooking(
                        firstWaitlistedUser.getUser().getEmail(),
                        booking.getShow().getId(),
                        booking.getBookingId(),
                        firstWaitlistedUser.getNumberOfTickets()
                );
            }
            waitListRepository.delete(firstWaitlistedUser);
        }

        return mapToBookingResponse(updatedBooking);
    }

    public ShowResponse mapToResponse(Show show) {
        List<SlotResponse> slotResp = new ArrayList<>();
        if (show.getSlots() != null) {
            for (Slot s : show.getSlots()) {
                SlotResponse sr = new SlotResponse(
                        s.getStartTime(),
                        s.getEndTime(),
                        s.getCapacity(),
                        s.getBookedSeats(),
                        s.getCapacity() - s.getBookedSeats()
                );
                slotResp.add(sr);
            }
        }

        return new ShowResponse(
                show.getId(),
                show.getTitle(),
                show.getGenre(),
                show.getDescription(),
                new OrganizerResponse(show.getOrganizer().getEmail()),
                slotResp
        );
    }

    public Show getTrendingShow() {
        List<Show> allShows = showService.getAllShows();
        Show trendingShow = null;
        long maxTicketsBooked = 0;

        for (Show show : allShows) {
            long totalBookedTickets = bookingRepository.findByShow(show).stream()
                    .mapToLong(Booking::getNumberOfTickets)
                    .sum();
            if (totalBookedTickets > maxTicketsBooked) {
                maxTicketsBooked = totalBookedTickets;
                trendingShow = show;
            }
        }
        if (trendingShow == null) {
            throw new ShowNotFoundException(SHOW_NOT_FOUND);
        }
        return trendingShow;
    }
}