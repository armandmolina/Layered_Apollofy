package cat.tecnocampus.apollofy.persistence;

import cat.tecnocampus.apollofy.domain.DJList;
import cat.tecnocampus.apollofy.domain.DJListTrackFragment;
import cat.tecnocampus.apollofy.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*TODO 4.5
   You may want to add a method to find a DJListTrackFragment that matches a given Track and a given DJList
   If you name the method correctly, you will NOT need to create a special sql query
 */
public interface DJListTrackFragmentRepository extends JpaRepository<DJListTrackFragment, Long> {
    Optional<DJListTrackFragment> findByTrackAndDjList(Track track, DJList djList);

}
