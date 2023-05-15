package cat.tecnocampus.apollofy.persistence;

import cat.tecnocampus.apollofy.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayListRepository extends JpaRepository<Playlist, Long> {

    @Query("select p from play_list p where p.owner.email like ?1")
    List<Playlist> findUserPlayLists(String email);

}
