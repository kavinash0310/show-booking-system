package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.ShowRequest;
import com.thinkify.show_booking_system.pojo.ShowResponse;

import java.util.List;

public interface ShowService {
    Show createShow(ShowRequest showReq, User organizer);
    List<ShowResponse> getRankedShowByGenre(String genreStr);
    List<ShowResponse> getAllShowResponses();
    Show getShowById(Long showId);
    List<Show> getAllShows();
    ShowResponse mapToResponse(Show show);
}
