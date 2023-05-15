package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.domain.LikeTrack;
import cat.tecnocampus.apollofy.domain.Track;
import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.LikeTrackRepository;
import cat.tecnocampus.apollofy.persistence.TrackRepository;
import cat.tecnocampus.apollofy.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikesController {
    private final LikeTrackRepository likeTrackRepository;
    private final UserRepository userRepository;

    private final TrackRepository trackRepository;

    public LikesController(LikeTrackRepository likeTrackRepository, UserRepository userRepository, TrackRepository trackRepository) {
        this.likeTrackRepository = likeTrackRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
    }

    public List<Track> getUserLikedTracks(String email) {
        UserFy user = userRepository.findByEmail(email).orElseThrow(() -> new ElementNotFoundInBBDD("User " + email));
        List<LikeTrack> likesTrack = likeTrackRepository.findByUserFy(user);
        return likesTrack.stream().map(lt -> lt.getTrack()).collect(Collectors.toList());
    }

    public void userLikesTrack(String email, Long trackId) {
        UserFy user = userRepository.findByEmail(email).orElseThrow(() -> new ElementNotFoundInBBDD("User " + email));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + trackId));
        LikeTrack likeTrack = new LikeTrack(user, track);

        likeTrackRepository.save(likeTrack);
    }
}
