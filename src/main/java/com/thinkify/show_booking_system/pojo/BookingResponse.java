package com.thinkify.show_booking_system.pojo;

public class BookingResponse {
    private Long bookingId;
    private String userEmail;
    private OrganizerResponse organizerEmail;
    private int numberOfTickets;
    private String status;
    private SlotResponse slot;

    public BookingResponse(Long bookingId, String userEmail, OrganizerResponse organizerEmail, int numberOfTickets, String status, SlotResponse slot) {
        this.bookingId = bookingId;
        this.userEmail = userEmail;
        this.organizerEmail = organizerEmail;
        this.numberOfTickets = numberOfTickets;
        this.status = status;
        this.slot = slot;
    }

    public BookingResponse() {
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public OrganizerResponse getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(OrganizerResponse organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SlotResponse getSlot() {
        return slot;
    }

    public void setSlot(SlotResponse slot) {
        this.slot = slot;
    }
}
