package com.thinkify.show_booking_system.controller;

import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.ShowRequest;
import com.thinkify.show_booking_system.pojo.ShowResponse;
import com.thinkify.show_booking_system.security.JwtUtil;
import com.thinkify.show_booking_system.service.ShowService;
import com.thinkify.show_booking_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final UserService userService;

    private final ShowService showService;

    private final JwtUtil jwtUtil;

    public ShowController(UserService userService, ShowService showService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.showService = showService;
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping
    public ResponseEntity<ShowResponse> createShow(@RequestBody ShowRequest showReq,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        User user = userService.getUserByEmail(email);

        Show newShow = showService.createShow(showReq, user);
        return ResponseEntity.ok(showService.mapToResponse(newShow));
    }

    @GetMapping
    public List<ShowResponse> getAllShows() {
        return showService.getAllShowResponses();
    }

    @GetMapping("/{showId}")
    public Show getShowById(@PathVariable Long showId) {
        return showService.getShowById(showId);
    }

    @GetMapping("/search")
    public List<ShowResponse> searchByGenre(@RequestParam String genre) {
        return showService.getRankedShowByGenre(genre);
    }
}