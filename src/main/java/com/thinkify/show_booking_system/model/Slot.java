package com.thinkify.show_booking_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer capacity;
    private Integer bookedSeats = 0;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    private List<Waitlist> waitlist = new ArrayList<>();

    public Slot() {
    }

    public Slot(LocalDateTime startTime, LocalDateTime endTime, Integer capacity, Integer bookedSeats) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Integer bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Waitlist> getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(List<Waitlist> waitlist) {
        this.waitlist = waitlist;
    }
}
