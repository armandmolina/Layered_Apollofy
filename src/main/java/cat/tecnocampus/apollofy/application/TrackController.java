package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.dto.PopularTrack;
import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.domain.Genre;
import cat.tecnocampus.apollofy.domain.Track;
import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.GenreRepository;
import cat.tecnocampus.apollofy.persistence.LikeTrackRepository;
import cat.tecnocampus.apollofy.persistence.TrackRepository;
import cat.tecnocampus.apollofy.persistence.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrackController {
    private final GenreRepository genreRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final LikeTrackRepository likeTrackRepository;


    public TrackController(GenreRepository genreRepository, TrackRepository trackRepository, UserRepository userRepository, LikeTrackRepository likeTrackRepository) {
        this.genreRepository = genreRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.likeTrackRepository = likeTrackRepository;
    }

    public Track getTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + id));
    }

    public List<Track> getTracks() {
        return trackRepository.findAll();
    }

    @Transactional
    public void createNewTrack(Track track) {

        List<String> emails = track.getArtists().stream().map(a -> a.getEmail()).collect(Collectors.toList());
        List<UserFy> artists = userRepository.findAllByEmail(emails);
        artists.stream().forEach(a -> a.addAuthoredTrack(track));
        track.setArtists(artists);

        List<Genre> genres = getGenresFromDB(track);
        if (!genres.isEmpty()) {
            track.setGenres(new HashSet<Genre>(genres));
        }

        trackRepository.save(track);
    }

    @Transactional
    public void addArtistsToTrack(Long trackId, Set<String> artistsEmail) {
        List<UserFy> artists = userRepository.findAllByEmail(new ArrayList<String>(artistsEmail));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + trackId));
        artists.forEach(a -> a.addAuthoredTrack(track));
    }

    private List<Genre> getGenresFromDB(Track track) {
        List<Long> ids = track.getGenres().stream().map(Genre::getId).filter(gid -> gid!=null).collect(Collectors.toList());
        List<Genre> genres = genreRepository.findAllById(ids);

        return genres;
    }

    public Set<Genre> getTrackGenres(Long trackId) {
        return trackRepository.findById(trackId).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + trackId)).getGenres();
    }

    @Transactional
    public void addGenresToTrack(Long trackId, List<Long> genresId) {
        List<Genre> genres = genreRepository.findAllById(genresId);
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + trackId));

        track.addGenres(genres);
    }

    @Transactional
    public void removeGenreFromTrack(Long trackId, Long genreId) {
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + trackId));
        Genre genre = track.getGenres().stream().filter(g -> g.getId().equals(genreId)).findFirst().orElseThrow(() -> new ElementNotFoundInBBDD("Genre with id " + genreId));

        track.removeGenre(genre);
    }

    public List<PopularTrack> getPopularTracks(int size, LocalDateTime from, LocalDateTime to) {
        List<PopularTrack> result = likeTrackRepository.popularTracks(from, to, PageRequest.of(0,size));

        result.stream().forEach(pt -> pt.setArtistNames(getTrackArtistNames(pt.getTrackId())));

        return result;
    }

    private List<String> getTrackArtistNames(Long trackId) {
        return trackRepository.findById(trackId).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + trackId))
                .getArtists().stream().map(a -> a.getName()).collect(Collectors.toList());

    }

}
