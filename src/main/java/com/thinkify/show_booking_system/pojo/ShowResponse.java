package com.thinkify.show_booking_system.pojo;

import com.thinkify.show_booking_system.model.Genre;

import java.util.List;

public class ShowResponse {
    private Long id;
    private String title;
    private Genre genre;
    private String description;
    private OrganizerResponse organizer;
    private List<SlotResponse> slots;

    public ShowResponse() {}

    public ShowResponse(Long id, String title, Genre genre, String description,
                        OrganizerResponse organizer, List<SlotResponse> slots) {
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

    public Genre getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public OrganizerResponse getOrganizer() {
        return organizer;
    }

    public List<SlotResponse> getSlots() {
        return slots;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrganizer(OrganizerResponse organizer) {
        this.organizer = organizer;
    }

    public void setSlots(List<SlotResponse> slots) {
        this.slots = slots;
    }
}