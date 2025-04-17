package com.thinkify.show_booking_system.service.Impl;

import com.thinkify.show_booking_system.exception.ShowNotFoundException;
import com.thinkify.show_booking_system.model.*;
import com.thinkify.show_booking_system.pojo.OrganizerResponse;
import com.thinkify.show_booking_system.pojo.ShowRequest;
import com.thinkify.show_booking_system.pojo.ShowResponse;
import com.thinkify.show_booking_system.pojo.SlotResponse;
import com.thinkify.show_booking_system.repository.ShowRepository;
import com.thinkify.show_booking_system.service.ShowService;
import com.thinkify.show_booking_system.service.SlotRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.thinkify.show_booking_system.constant.Constants.*;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final SlotRanking slotRanking;

    @Autowired
    public ShowServiceImpl(ShowRepository showRepository, SlotRanking slotRanking) {
        this.showRepository = showRepository;
        this.slotRanking = slotRanking;
    }
    public Show createShow(ShowRequest showReq, User organizer) {
        if (organizer == null || organizer.getRole() != Role.ORGANIZER) {
            throw new AccessDeniedException(INVALID_USER);
        }

        Show newShow = new Show(
                showReq.getTitle(),
                showReq.getDescription(),
                Genre.valueOf(showReq.getGenre()),
                organizer
        );

        if (showReq.getSlots() != null) {
            List<Slot> showSlots = new ArrayList<>();
            for (var s : showReq.getSlots()) {
                LocalDateTime st = s.getStartTime();
                LocalDateTime en = s.getEndTime();

                if (st == null || en == null || !st.isBefore(en)) {
                    throw new IllegalArgumentException(INVALID_SLOT);
                }

                for (Slot exSlot : showSlots) {
                    if (isOverlap(st, en, exSlot.getStartTime(), exSlot.getEndTime())) {
                        throw new IllegalArgumentException(SLOT_OVERLAP);
                    }
                }

                Slot slot = new Slot(st, en, s.getCapacity(), s.getBookedSeats());
                showSlots.add(slot);
            }
            newShow.setSlots(showSlots);
        }

        return showRepository.save(newShow);
    }


    public List<ShowResponse> getRankedShowByGenre(String genreStr) {
        Genre genre = Genre.valueOf(genreStr.toUpperCase());
        List<Show> shows = showRepository.findByGenre(genre);

        return shows.stream().map(show -> {
            List<Slot> rankedSlots = slotRanking.rankSlots(show.getSlots());
            show.setSlots(rankedSlots);
            return mapToResponse(show);
        }).collect(Collectors.toList());
    }

    private boolean isOverlap(LocalDateTime s1, LocalDateTime e1, LocalDateTime s2, LocalDateTime e2) {
        return s1.isBefore(e2) && s2.isBefore(e1);
    }

    public ShowResponse mapToResponse(Show show) {
        List<SlotResponse> slotResp = new ArrayList<>();
        if (show.getSlots() != null) {
            for (Slot s : show.getSlots()) {
                SlotResponse sr = new SlotResponse(
                        s.getStartTime(),
                        s.getEndTime(),
                        s.getCapacity(),
                        s.getBookedSeats(),
                        s.getCapacity() - s.getBookedSeats()
                );
                slotResp.add(sr);
            }
        }

        return new ShowResponse(
                show.getId(),
                show.getTitle(),
                show.getGenre(),
                show.getDescription(),
                new OrganizerResponse(show.getOrganizer().getEmail()),
                slotResp
        );
    }


    public List<ShowResponse> getAllShowResponses() {
        return showRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Show getShowById(Long showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new ShowNotFoundException(SHOW_NOT_FOUND + showId));
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }
}