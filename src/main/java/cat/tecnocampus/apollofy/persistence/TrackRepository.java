package cat.tecnocampus.apollofy.persistence;

import cat.tecnocampus.apollofy.application.dto.PopularGenre;
import cat.tecnocampus.apollofy.domain.Track;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
