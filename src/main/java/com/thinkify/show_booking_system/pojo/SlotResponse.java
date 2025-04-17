package com.thinkify.show_booking_system.pojo;

import java.time.LocalDateTime;

public class SlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;
    private int bookedSeats;
    private int availableSeats;

    public SlotResponse() {
    }

    public SlotResponse(LocalDateTime startTime, LocalDateTime endTime, int capacity, int bookedSeats, int availableSeats) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
        this.availableSeats = availableSeats;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
