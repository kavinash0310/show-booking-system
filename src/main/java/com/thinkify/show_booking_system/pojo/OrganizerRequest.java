package com.thinkify.show_booking_system.pojo;

import lombok.Data;

@Data
public class OrganizerRequest {
    private String email;

    public OrganizerRequest(String email) {
        this.email = email;
    }
}
