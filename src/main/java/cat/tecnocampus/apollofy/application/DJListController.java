package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.dto.DJListTrackDTO;
import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.domain.*;
import cat.tecnocampus.apollofy.persistence.DJListRepository;
import cat.tecnocampus.apollofy.persistence.DJListTrackFragmentRepository;
import cat.tecnocampus.apollofy.persistence.TrackRepository;
import cat.tecnocampus.apollofy.persistence.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DJListController {

    private DJListRepository djListRepository;
    private UserRepository userRepository;
    private TrackRepository trackRepository;
    private DJListTrackFragmentRepository djListTrackFragmentRepository;

    public DJListController(DJListRepository djListRepository, UserRepository userRepository,
                            TrackRepository trackRepository,
                            DJListTrackFragmentRepository djListTrackFragmentRepository) {
        this.djListRepository = djListRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.djListTrackFragmentRepository = djListTrackFragmentRepository;
    }

    /* TODO 4.2
        Implement a method to add track fragments to existing DJLists. For example:
        public void addTracksToDJlistWithTimeRange(String userEmail, Long DJListId, List<PlaylistTrackDTO> tracksDTO)

        Also you may want to receive a list of objects with the following attributes:
        Long trackId, Long startTimeMillis, Long endTimeMillis.
        You can use "application/dto/DJListTrackDTO for this matter (see _TODO 4.3)

        You'll need to implement a method in the DJListTrackFragmentRepository to get the DJListTrackFragments that point
        to a given DJList and Track (see _TODO 4.5)

        This method has to verify the following constraints and implement the specified behaviour:
        - The playlist with the provided identifier exists.
        - The user with the provided identifier exists.
        - The provided user is the owner of the playlist.
        - For each of the PlaylistTrackDTO in "tracksDTO" parameter:
              -you must verify that the track exists.
              -in the case that the track is already associated to that playlist, then startTimeMillis and endTimeMillis
               are updated in its corresponding PlaylistTrack object.
              -in case the track is not associated to the playlist yet, then you must create a new PlaylistTrack object,
               linking the track to the playlist and specifying startTimeMillis and endTimeMillis accordingly.
       */
    @Transactional
    public void addTracksToPlaylistWithTimeRange(String email, Long playListId, List<DJListTrackDTO> tracksDTO) {
        DJList djList = djListRepository.findById(playListId).orElseThrow(() -> new ElementNotFoundInBBDD("Play list doesn't exist"));
        UserFy user = userRepository.findByEmail(email).orElseThrow(() -> new ElementNotFoundInBBDD("User " + email));

        if(!djList.getOwner().equals(user)) {
            throw new RuntimeException("You are not the playlist owner.");
        }

        for (DJListTrackDTO DJListTrackDTO : tracksDTO) {
            Track trackDB = trackRepository
                    .findById(DJListTrackDTO.trackId())
                    .orElseThrow(() -> new ElementNotFoundInBBDD("Track with id " + DJListTrackDTO.trackId()));


            addTrackToPlaylist(djList, DJListTrackDTO, trackDB);

        }

    }

    private void addTrackToPlaylist(DJList djList, DJListTrackDTO DJListTrackDTO, Track trackDB) {
        Optional<DJListTrackFragment> playlistTrackOptional = djListTrackFragmentRepository.findByTrackAndDjList(trackDB, djList);

        if (playlistTrackOptional.isPresent()) {
            DJListTrackFragment DJListTrackFragment = playlistTrackOptional.get();

            DJListTrackFragment.setStartTimeMillis(DJListTrackDTO.startTimeMillis());
            DJListTrackFragment.setEndTimeMillis(DJListTrackDTO.endTimeMillis());

        } else {

            DJListTrackFragment DJListTrackFragment = new DJListTrackFragment(DJListTrackDTO.startTimeMillis(),
                    DJListTrackDTO.endTimeMillis(),
                    trackDB,
                    djList);
            djListTrackFragmentRepository.save(DJListTrackFragment);
        }
    }
}
