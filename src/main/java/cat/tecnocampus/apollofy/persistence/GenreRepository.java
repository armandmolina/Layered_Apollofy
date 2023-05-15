package cat.tecnocampus.apollofy.persistence;

import cat.tecnocampus.apollofy.application.dto.PopularGenre;
import cat.tecnocampus.apollofy.domain.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    public Slice<Genre> findBy(Pageable pageable);

    @Query("select genres.name as name, count(genres) as countTracks from Track t inner join t.genres genres group by genres.id order by countTracks desc")
    List<PopularGenre> findMostUsedGenres(Pageable pageable);

}
