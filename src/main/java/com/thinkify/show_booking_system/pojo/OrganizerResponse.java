package com.thinkify.show_booking_system.pojo;

public class OrganizerResponse {
    private String email;

    public OrganizerResponse(String email) {
        this.email = email;
    }

    public OrganizerResponse() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
