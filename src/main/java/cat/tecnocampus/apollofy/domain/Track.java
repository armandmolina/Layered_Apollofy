package cat.tecnocampus.apollofy.domain;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//TODO 1.0 Somewhere in this class you need to add some annotations to validate the title and the duration
@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[A-Z][a-z0-9\s_-]*$", message = "Track title must begin witb capital letter")
    @NotNull
    private String title;

    @Column(name = "duration_seconds")
    @Min(5)
    @Max(300)
    private Long durationSeconds;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "track_genre",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "track_artist",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<UserFy> artists =new HashSet<>();


    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void addGenres(List<Genre> genreList) {
        genres.addAll(genreList);
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Set<UserFy> getArtists() {
        return artists;
    }

    public void setArtists(List<UserFy> artists) {
        this.artists  = new HashSet<>(artists);
    }

    public void addArtist(UserFy artist) {
        artists.add(artist);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Track track = (Track) o;
        return id != null && Objects.equals(id, track.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
