package cat.tecnocampus.apollofy.persistence;

import cat.tecnocampus.apollofy.domain.DJList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DJListRepository extends JpaRepository<DJList, Long> {
}
