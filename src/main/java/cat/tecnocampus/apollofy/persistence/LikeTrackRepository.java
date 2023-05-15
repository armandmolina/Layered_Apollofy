package cat.tecnocampus.apollofy.persistence;

import cat.tecnocampus.apollofy.application.dto.PopularTrack;
import cat.tecnocampus.apollofy.domain.LikeTrack;
import cat.tecnocampus.apollofy.domain.UserFy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LikeTrackRepository extends JpaRepository<LikeTrack, Long> {
    @Query("select new cat.tecnocampus.apollofy.application.dto.PopularTrack(track.id, track.title, count(track)) from LikeTrack lt " +
            "inner join lt.track track " +
            "where lt.liked = TRUE and lt.date >= :from and lt.date <= :to " +
            "group by track.id order by count(track) desc")
    List<PopularTrack> popularTracks(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    List<LikeTrack> findByUserFy(UserFy userFy);


}
