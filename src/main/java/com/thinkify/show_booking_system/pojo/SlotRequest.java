package com.thinkify.show_booking_system.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SlotRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;
    private int bookedSeats;

    public SlotRequest() {
    }

    public SlotRequest(LocalDateTime startTime, LocalDateTime endTime, int capacity, int bookedSeats) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
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
}
