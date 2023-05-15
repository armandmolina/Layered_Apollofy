package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.dto.PopularGenre;
import cat.tecnocampus.apollofy.domain.Genre;
import cat.tecnocampus.apollofy.persistence.GenreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Slice<Genre> getGenresOrderedPaginated(int page, int size) {
        return genreRepository.findBy(PageRequest.of(page, size, Sort.by("name")));
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public List<PopularGenre> getTopGenresAsInTracks(int size) {
        return genreRepository.findMostUsedGenres(PageRequest.of(0,size));
    }
}
