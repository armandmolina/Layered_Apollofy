package cat.tecnocampus.apollofy.api;

import cat.tecnocampus.apollofy.application.*;
import cat.tecnocampus.apollofy.application.dto.PopularGenre;
import cat.tecnocampus.apollofy.application.dto.PopularTrack;
import cat.tecnocampus.apollofy.application.dto.UserProjection;
import cat.tecnocampus.apollofy.domain.Genre;
import cat.tecnocampus.apollofy.domain.Playlist;
import cat.tecnocampus.apollofy.domain.Track;
import cat.tecnocampus.apollofy.domain.UserFy;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//TODO 1.2 somewhere here you need to add annotations to validate tracks

//TODO 1.3 you also need to add some classes to handle validation errors (you can find them in the Tinder example, among other places)
@RestController
@RequestMapping("/api/")
public class ApollofyRestController {

    private final UserPlayListController userPlayListController;
    private final LikesController likesController;
    private final GenreController genreController;

    private final TrackController trackController;

    private final FanController fanController;

    public ApollofyRestController(UserPlayListController userPlayListController, LikesController likesController,
                                  GenreController genreController, TrackController trackController, FanController fanController) {
        this.userPlayListController = userPlayListController;
        this.likesController = likesController;
        this.genreController = genreController;
        this.trackController = trackController;
        this.fanController = fanController;
    }

    @PostMapping("/tracks")
    public void createTrack(@RequestBody @Valid Track track) {
        trackController.createNewTrack(track);
    }

    @GetMapping("/tracks")
    public List<Track> getTracks() {
        return trackController.getTracks();
    }

    @GetMapping("/tracks/{id}")
    public Track getTrack(@PathVariable Long id) {
        return trackController.getTrack(id);
    }

    @GetMapping("/tracks/{id}/genres")
    public Set<Genre> getTrackGenres(@PathVariable Long id) {
        return trackController.getTrackGenres(id);
    }

    @PutMapping("/tracks/{id}/genres")
    public void addTrackGenres(@PathVariable Long id, @RequestBody List<Long> genresId) {
        trackController.addGenresToTrack(id, genresId);
    }

    @PutMapping("/tracks/{trackId}/artists")
    public void addArtistToTrack(@PathVariable Long trackId, @RequestBody Set<String> artistsEmail) {
        trackController.addArtistsToTrack(trackId, artistsEmail);
    }

    @GetMapping("/genres")
    public Slice<Genre> getGenres(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return genreController.getGenresOrderedPaginated(page, size);
    }

    @DeleteMapping("/genres/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreController.deleteGenre(id);
    }

    @DeleteMapping("/tracks/{trackId}/genres/{genreId}")
    public void deleteGenreFromTrack(@PathVariable Long trackId, @PathVariable Long genreId) {
        trackController.removeGenreFromTrack(trackId, genreId);
    }

    @GetMapping("/users")
    public List<UserFy> getUsers() {
        return userPlayListController.getUsers();
    }

    @GetMapping("/me")
    public UserFy getUserMe(Principal principal) {
        return userPlayListController.getMe(principal.getName());
    }

    @GetMapping("/me/tracks")
    public List<Track> getUserTracks(Principal principal) {
        return userPlayListController.getMe(principal.getName()).getAuthoredTracks();
    }

    @GetMapping("/me/playLists")
    public List<Playlist> getUserPlayLists(Principal principal) {
        return userPlayListController.getUserPlayLists(principal.getName());
    }

    @GetMapping("/playLists")
    public List<Playlist> getPlayLists() {
        return userPlayListController.getPlayLists();
    }

    @GetMapping("/playLists/{id}")
    public Playlist getPlayList(@PathVariable Long id) {
        return userPlayListController.getPlayListById(id);
    }

    @PostMapping("/me/playLists")
    public void postPlayLists(@RequestBody Playlist playList, Principal principal) {
        userPlayListController.createPlayList(playList, principal.getName());
    }

    @GetMapping("/me/likedTracks")
    public List<Track> getLikedTracks(Principal principal) {
        return likesController.getUserLikedTracks(principal.getName());
    }

    @PutMapping("/playLists/{id}/tracks")
    public void addTracksToPlayList(@PathVariable Long id, @RequestBody List<Long> trackIds) {
        userPlayListController.addTracksToPlayList(id, trackIds);
    }


    @PutMapping("/me/likedTracks/{id}")
    public void likeTrack(Principal principal, @PathVariable Long id) {
        likesController.userLikesTrack(principal.getName(), id);
    }

    @GetMapping("/top/genres")
    List<PopularGenre> getTopGenres(@RequestParam(defaultValue = "5") int size) {
        return genreController.getTopGenresAsInTracks(size);
    }

    @GetMapping("/top/tracks")
    List<PopularTrack> getPopularTracks(@RequestParam(defaultValue = "3") int size,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate from,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate to) {

        if (from == null) from = LocalDate.now().minusYears(10);
        if (to == null) to = LocalDate.now().plusDays(1);
        System.out.println("going to return ");
        return trackController.getPopularTracks(size, from.atStartOfDay(), to.atStartOfDay());
    }

    @GetMapping("/userProjection")
    public List<UserProjection> getUserProjections() {
        return userPlayListController.getUserProjections();
    }

    @PostMapping("/user/fanOf/{destination}")
    public void newFanOf(Principal principal, @PathVariable String destination) {
        fanController.createFan(principal.getName(), destination);
    }
}

