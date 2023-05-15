package cat.tecnocampus.apollofy.api;

import cat.tecnocampus.apollofy.application.UserPlayListController;
import cat.tecnocampus.apollofy.application.dto.DJListTrackDTO;
import cat.tecnocampus.apollofy.domain.DJListTrackFragment;
import cat.tecnocampus.apollofy.domain.Track;
import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.DJListTrackFragmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ApollofyRestControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserPlayListController userPlayListController;

    @Autowired
    private DJListTrackFragmentRepository DJListTrackFragmentRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void findAllTracks() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tracks")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Sound of silence"));
        assertTrue(result.getResponse().getContentAsString().contains("Vuela con el viento"));
        assertTrue(result.getResponse().getContentAsString().contains("La lluna la pruna"));
    }

    @Test
    @WithMockUser(username = "jalvarez@tecnocampus.cat", roles = "FREE")
    void findUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/me")).andExpect(status().isOk()).andReturn();
        UserFy userFy = objectMapper.readValue(result.getResponse().getContentAsString(), UserFy.class);
        assertEquals(userFy.getEmail(), "jalvarez@tecnocampus.cat");
        assertEquals(userFy.getName(), "Josep");
        assertEquals(userFy.getSecondName(), "Alvarez");
    }

    @Test
    @WithMockUser(username = "jalcobe@tecnocampus.cat", roles = {"PROFESSIONAL"})
    void addTracksToPlayListWithTimeRange() throws Exception {

        // Create PlayTrackDTO to add two tracks to a playlist with their time range
        final DJListTrackDTO track1 = new DJListTrackDTO(1L, 3000L, 4000L);
        final DJListTrackDTO track2 = new DJListTrackDTO(2L, 2302L, 6789L);

        List<DJListTrackDTO> DJListTrackDTOList = Arrays.asList(new DJListTrackDTO[]{track1, track2});

        // Convert Java DTO to JSON.
        String body = objectMapper.writeValueAsString(DJListTrackDTOList);

        mockMvc.perform(post("/api/djlist/1/tracks")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated()) It should be 201 Created as Spotify official API doc, but we use 200
                .andExpect(status().isOk())
                .andReturn();

        // Get PlaylistTracks to ensure that the tracks have been added correctly in the previous POST API call:
        // post("/api/playlist/1/tracks")
        MvcResult result = mockMvc.perform(get("/api/djlist/1/tracks")).andExpect(status().isOk()).andReturn();
        //assertTrue(result.getResponse().getContentAsString().contains("3000"));
        // Convert HTTP response body from JSON to Java
        List<DJListTrackFragment> DJListTrackFragments = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        /*
        Check that the API get("/api/playlist/1/tracks") returns the same "playlistTrack" that we have associated with the playlist
        by means of the current JUnit test. To implement the test, the information of the DTO that we have passed in the body of the
        POST request post("/api/playlist/1/tracks") is compared with the information returned by the API: get("/api/playlist/1/tracks")

        For simplicity, it has not been specified as a requirement that the Playlist tracks must be in order.
        That is why the verification code is a little more complex.

         */
        assertTrue(DJListTrackFragments.stream().anyMatch(playlistTrack -> playlistTrackEqualsDTO(playlistTrack, track1)));
        assertTrue(DJListTrackFragments.stream().anyMatch(playlistTrack -> playlistTrackEqualsDTO(playlistTrack, track2)));
    }

    private static boolean playlistTrackEqualsDTO(DJListTrackFragment DJListTrackFragment, DJListTrackDTO trackToCheck) {
        return DJListTrackFragment.getTrack().getId().equals(trackToCheck.trackId()) &&
                DJListTrackFragment.getStartTimeMillis().equals(trackToCheck.startTimeMillis()) &&
                DJListTrackFragment.getEndTimeMillis().equals(trackToCheck.endTimeMillis());
    }

    // Tests for QUESTION 1
    @Test
    @WithMockUser("un-authorized@tecnocampus.cat")
    void unAuthorizedUserQ1() throws Exception {
        mockMvc.perform(get("/api/tracks")).andExpect(status().isOk());

        mockMvc.perform(get("/api/tracks/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/me/tracks")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/me")).andExpect(status().isForbidden());

        String body = createTrack("Testing song", 123L);
        mockMvc.perform(post("/api/tracks").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/tracks/1/artists").content("[\"jalcobe@tecnocampus.cat\"]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/tracks/1/genres").content("[2,4]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/me/likedTracks/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/top/genres?size=3")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/top/tracks")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "jalvarez@tecnocampus.cat", roles = "FREE")
    void freeUserQ1() throws Exception {
        mockMvc.perform(get("/api/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/tracks/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/me/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/me")).andExpect(status().isOk());

        String body = createTrack("Testing song", 123L);
        mockMvc.perform(post("/api/tracks").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/tracks/1/artists").content("[\"jalcobe@tecnocampus.cat\"]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/tracks/1/genres").content("[2,4]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/me/likedTracks/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/top/genres?size=3")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/top/tracks")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "mperez@tecnocampus.cat", roles = {"PREMIUM"})
    void premiumUserQ1() throws Exception {
        mockMvc.perform(get("/api/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/tracks/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/me/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/me")).andExpect(status().isOk());

        String body = createTrack("Testing song", 123L);
        mockMvc.perform(post("/api/tracks").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/tracks/1/artists").content("[\"jalcobe@tecnocampus.cat\"]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/tracks/1/genres").content("[2,4]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/me/likedTracks/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/top/genres?size=3")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/top/tracks")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "jalcobe@tecnocampus.cat", roles = {"PROFESSIONAL"})
    void professionalUserQ1() throws Exception {
        mockMvc.perform(get("/api/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/tracks/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/me/tracks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/me")).andExpect(status().isOk());

        String body = createTrack("Testing song", 123L);
        mockMvc.perform(post("/api/tracks").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/tracks/1/artists").content("[\"jalcobe@tecnocampus.cat\"]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/tracks/1/genres").content("[2,4]").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/me/likedTracks/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/users")).andExpect(status().isOk());
        mockMvc.perform(get("/api/top/genres?size=3")).andExpect(status().isOk());
        mockMvc.perform(get("/api/top/tracks")).andExpect(status().isOk());
    }

    private String createTrack(String title, Long duration) throws JsonProcessingException {
        Track track = new Track();
        track.setTitle(title);
        track.setDurationSeconds(duration);
        String body = objectMapper.writeValueAsString(track);
        return body;
    }


    //Test for QUESTION 2
    @Test
    @WithMockUser(username = "jalcobe@tecnocampus.cat", roles = {"PROFESSIONAL"})
    void malFormedTrack() throws Exception{
        String body = createTrack("testing song", 1L);
        MvcResult result = mockMvc.perform(post("/api/tracks").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("title"));
        assertTrue(result.getResponse().getContentAsString().contains("durationSeconds"));
    }

    //Test for QUESTION 3
    @Test
    @WithMockUser(username = "jalcobe@tecnocampus.cat", roles = {"PROFESSIONAL"})
    void nonExistingTrack() throws Exception {
        mockMvc.perform(get("/api/tracks/1000")).andExpect(status().isNotFound());
    }
}
