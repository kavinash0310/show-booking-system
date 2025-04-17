package com.thinkify.show_booking_system.controller;

import com.thinkify.show_booking_system.model.Genre;
import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.model.Slot;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.OrganizerRequest;
import com.thinkify.show_booking_system.pojo.ShowRequest;
import com.thinkify.show_booking_system.pojo.ShowResponse;
import com.thinkify.show_booking_system.pojo.SlotRequest;
import com.thinkify.show_booking_system.security.JwtUtil;
import com.thinkify.show_booking_system.service.ShowService;
import com.thinkify.show_booking_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ShowControllerTest {

    @InjectMocks
    private ShowController showController;

    @Mock
    private ShowService showService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShow() {
        OrganizerRequest organizerRequest = new OrganizerRequest("organizer@example.com");
        List<SlotRequest> slotRequests = List.of(new SlotRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                100,
                0
        ));

        ShowRequest showRequest = new ShowRequest(
                null,
                "Test Show",
                "COMEDY",
                "Test Description",
                organizerRequest,
                slotRequests
        );

        String authHeader = "Bearer mockToken";
        when(jwtUtil.extractEmail(any(String.class))).thenReturn("organizer@example.com");

        User mockUser = new User();
        mockUser.setEmail("organizer@example.com");
        when(userService.getUserByEmail(any(String.class))).thenReturn(mockUser);

        Show mockShow = new Show();
        mockShow.setTitle("Test Show");
        mockShow.setGenre(Genre.COMEDY);
        mockShow.setDescription("Test Description");

        User organizer = new User();
        organizer.setEmail("organizer@example.com");
        mockShow.setOrganizer(organizer);

        Slot mockSlot = new Slot();
        mockSlot.setStartTime(LocalDateTime.now().plusHours(1));
        mockSlot.setEndTime(LocalDateTime.now().plusHours(2));
        mockSlot.setCapacity(100);
        mockSlot.setBookedSeats(0);
        mockShow.setSlots(List.of(mockSlot));

        when(showService.createShow(any(ShowRequest.class), any(User.class))).thenReturn(mockShow);

        ShowResponse mockShowResponse = new ShowResponse();
        mockShowResponse.setTitle(mockShow.getTitle());
        mockShowResponse.setGenre(mockShow.getGenre());
        mockShowResponse.setDescription(mockShow.getDescription());
        when(showService.mapToResponse(mockShow)).thenReturn(mockShowResponse);

        ResponseEntity<ShowResponse> response = showController.createShow(showRequest, authHeader);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(Genre.COMEDY, response.getBody().getGenre());
        assertEquals("Test Show", response.getBody().getTitle());
        assertEquals("Test Description", response.getBody().getDescription());
    }

    @Test
    void testGetAllShows() {
        Show mockShow1 = new Show();
        mockShow1.setTitle("Show 1");
        Show mockShow2 = new Show();
        mockShow2.setTitle("Show 2");
        when(showService.getAllShowResponses()).thenReturn(List.of(new ShowResponse(), new ShowResponse()));
        List<ShowResponse> shows = showController.getAllShows();
        assertEquals(2, shows.size());
        verify(showService, times(1)).getAllShowResponses();
    }

    @Test
    void testSearchByGenre() {
        Show mockShow = new Show();
        mockShow.setTitle("Genre Show");
        when(showService.getRankedShowByGenre(any(String.class))).thenReturn(List.of(new ShowResponse()));
        List<ShowResponse> shows = showController.searchByGenre("SINGING");
        assertNotNull(shows);
        assertFalse(shows.isEmpty());
        verify(showService, times(1)).getRankedShowByGenre("SINGING");
    }
}
