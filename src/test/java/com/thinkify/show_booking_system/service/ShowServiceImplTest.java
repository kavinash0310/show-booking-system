package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.exception.ShowNotFoundException;
import com.thinkify.show_booking_system.model.Genre;
import com.thinkify.show_booking_system.model.Role;
import com.thinkify.show_booking_system.model.Show;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.OrganizerRequest;
import com.thinkify.show_booking_system.pojo.ShowRequest;
import com.thinkify.show_booking_system.pojo.SlotRequest;
import com.thinkify.show_booking_system.repository.ShowRepository;
import com.thinkify.show_booking_system.service.Impl.ShowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ShowServiceImplTest {

    @InjectMocks
    private ShowServiceImpl showService;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private SlotRanking slotRanking;

    @Mock
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShow() {
        List<SlotRequest> slotRequests = List.of(new SlotRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                100,
                0
        ));
        OrganizerRequest organizerRequest = new OrganizerRequest("organizer@example.com");

        ShowRequest showRequest = new ShowRequest(
                null,
                "Test Show",
                "COMEDY",
                "Test Description",
                organizerRequest,
                slotRequests
        );

        User mockUser = mock(User.class);
        when(mockUser.getRole()).thenReturn(Role.ORGANIZER);
        when(mockUser.getEmail()).thenReturn("organizer@example.com");

        Show newShow = new Show();
        newShow.setTitle("Test Show");
        when(showRepository.save(any(Show.class))).thenReturn(newShow);
        Show createdShow = showService.createShow(showRequest, mockUser);
        assertNotNull(createdShow);
        assertEquals("Test Show", createdShow.getTitle());
        verify(showRepository, times(1)).save(any(Show.class));
    }

    @Test
    void testGetShowById() {
        Show mockShow = new Show();
        mockShow.setId(1L);
        mockShow.setTitle("Test Show");
        when(showRepository.findById(1L)).thenReturn(Optional.of(mockShow));
        Show show = showService.getShowById(1L);
        assertNotNull(show);
        assertEquals("Test Show", show.getTitle());
    }

    @Test
    void testGetShowById_NotFound() {
        when(showRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ShowNotFoundException.class, () -> {
            showService.getShowById(1L);
        });
    }

    @Test
    void testGetAllShowResponses() {
        Show mockShow = new Show();
        mockShow.setTitle("Show 1");
        User organizer = new User();
        organizer.setEmail("organizer@example.com");
        mockShow.setOrganizer(organizer);
        mockShow.setGenre(Genre.COMEDY);
        mockShow.setSlots(new ArrayList<>());
        when(showRepository.findAll()).thenReturn(List.of(mockShow));
        var showResponses = showService.getAllShowResponses();
        assertEquals(1, showResponses.size());
    }

    @Test
    void testGetRankedShowByGenre() {
        Show mockShow = new Show();
        mockShow.setTitle("Comedy Show");

        User organizer = new User();
        organizer.setEmail("organizer@example.com");
        mockShow.setOrganizer(organizer);
        mockShow.setGenre(Genre.COMEDY);
        mockShow.setSlots(new ArrayList<>());

        when(showRepository.findByGenre(any())).thenReturn(List.of(mockShow));
        when(slotRanking.rankSlots(any())).thenReturn(mockShow.getSlots());

        var rankedShows = showService.getRankedShowByGenre("COMEDY");

        assertNotNull(rankedShows);
        assertFalse(rankedShows.isEmpty());
    }
}
