package com.thinkify.show_booking_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne
    @JoinColumn(name = "Slot_id")
    private Slot Slot;

    private String status;

    private int numberOfTickets;

    public Booking() {}

    public Booking(Long bookingId, User user, Slot slot, Show show, String status, int numberOfTickets) {
        this.bookingId = bookingId;
        this.user = user;
        this.Slot = slot;
        this.show = show;
        this.status = status;
        this.numberOfTickets = numberOfTickets;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Slot getSlot() {
        return Slot;
    }

    public void setSlot(Slot slot) {
        this.Slot = slot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}



