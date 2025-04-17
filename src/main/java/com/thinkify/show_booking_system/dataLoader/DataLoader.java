package com.thinkify.show_booking_system.dataLoader;

import com.thinkify.show_booking_system.model.*;
import com.thinkify.show_booking_system.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @PostConstruct
    public void loadData() {
        User organizer1 = new User(null, "organizer1@gmail.com", "pass123", Role.ORGANIZER);
        User organizer2 = new User(null, "organizer2@gmail.com", "pass123", Role.ORGANIZER);
        User user1 = new User(null, "user1@gmail.com", "pass123", Role.CUSTOMER);
        User user2 = new User(null, "user2@gmail.com", "pass123", Role.CUSTOMER);
        userRepository.saveAll(List.of(organizer1, organizer2, user1, user2));

        Show show1 = new Show();
        show1.setTitle("TMKOC");
        show1.setGenre(Genre.COMEDY);
        show1.setDescription("Comedy event");
        show1.setOrganizer(organizer1);

        Show show2 = new Show();
        show2.setTitle("The Sonu Nigam Live Event");
        show2.setGenre(Genre.SINGING);
        show2.setDescription("Singing Event");
        show2.setOrganizer(organizer2);

        Slot slot1 = new Slot();
        slot1.setStartTime(LocalDateTime.of(2025, 4, 16, 9, 0));
        slot1.setEndTime(LocalDateTime.of(2025, 4, 16, 10, 0));
        slot1.setShow(show1);
        slot1.setCapacity(50);
        slot1.setBookedSeats(0);

        Slot slot2 = new Slot();
        slot2.setStartTime(LocalDateTime.of(2025, 4, 16, 10, 0));
        slot2.setEndTime(LocalDateTime.of(2025, 4, 16, 11, 0));
        slot2.setShow(show2);
        slot2.setCapacity(50);
        slot2.setBookedSeats(12);

        show1.setSlots(List.of(slot1));
        show2.setSlots(List.of(slot2));

        showRepository.saveAll(List.of(show1, show2));
        slotRepository.saveAll(List.of(slot1, slot2));

        Ticket ticket1 = new Ticket();
        ticket1.setSlot(slot1);
        ticket1.setUser(user1);
        ticket1.setSeatCount(2);

        Ticket ticket2 = new Ticket();
        ticket2.setSlot(slot2);
        ticket2.setUser(user2);
        ticket2.setSeatCount(1);

        ticketRepository.saveAll(List.of(ticket1, ticket2));

        System.out.println("Data loaded");
    }
}