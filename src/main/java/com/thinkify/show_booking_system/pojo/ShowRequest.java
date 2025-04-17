package com.thinkify.show_booking_system.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ShowRequest {
    private Long id;
    private String title;
    private String genre;
    private String description;
    private OrganizerRequest organizer;
    private List<SlotRequest> slots;

    public ShowRequest() {
    }

    public ShowRequest(Long id, String title, String genre, String description,
                        OrganizerRequest organizer, List<SlotRequest> slots) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.organizer = organizer;
        this.slots = slots;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public OrganizerRequest getOrganizer() {
        return organizer;
    }

    public List<SlotRequest> getSlots() {
        return slots;
    }
}