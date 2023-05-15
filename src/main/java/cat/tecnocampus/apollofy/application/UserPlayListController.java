package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.dto.UserProjection;
import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.domain.Playlist;
import cat.tecnocampus.apollofy.domain.Track;
import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserPlayListController {
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final PlayListRepository playListRepository;
    private final LikeTrackRepository likeTrackRepository;

    private final DJListTrackFragmentRepository DJListTrackFragmentRepository;

    public UserPlayListController(TrackRepository trackRepository,
                                  UserRepository userRepository, PlayListRepository playListRepository,
                                  LikeTrackRepository likeTrackRepository, DJListTrackFragmentRepository DJListTrackFragmentRepository) {
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.playListRepository = playListRepository;
        this.likeTrackRepository = likeTrackRepository;
        this.DJListTrackFragmentRepository = DJListTrackFragmentRepository;
    }


    public List<UserFy> getUsers() {
        return userRepository.findAll();
    }

    public List<Playlist> getPlayLists() {
        return playListRepository.findAll();
    }

    public Playlist getPlayListById(Long id) {
        return playListRepository.findById(id).orElseThrow(() -> new ElementNotFoundInBBDD("PlayList with id " + id));
    }

    @Transactional
    public void createPlayList(Playlist playList, String ownerEmail) {
        UserFy user = userRepository.findByEmail(ownerEmail).orElseThrow(() -> new ElementNotFoundInBBDD("User " + ownerEmail));
        playList.setOwner(user);

        playList.setTracks(getPlayListTracksFromDB(playList));
        playListRepository.save(playList);
    }

    private Set<Track> getPlayListTracksFromDB(Playlist playList) {
        return playList.getTracks().stream().map(t -> trackRepository.findById(t.getId()).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + t.getId())))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void addTracksToPlayList(Long playListId, List<Long> tracksId) {
        Playlist playlistDB = playListRepository.findById(playListId).orElseThrow(() -> new ElementNotFoundInBBDD("Play list doesn't exist"));
        tracksId.stream().map(tid -> trackRepository.findById(tid).orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + tid))).forEach(t -> playlistDB.addTrack(t));
    }

    public UserFy getMe(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ElementNotFoundInBBDD("User " + email));
    }

    public List<Playlist> getUserPlayLists(String email) {
        return playListRepository.findUserPlayLists(email);
    }


    public List<UserProjection> getUserProjections() {
        return userRepository.findUserProjection();
    }
}
