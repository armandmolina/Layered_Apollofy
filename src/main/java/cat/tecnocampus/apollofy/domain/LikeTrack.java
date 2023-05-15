package cat.tecnocampus.apollofy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class LikeTrack {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "liked")
    private Boolean liked;

    //https://thorben-janssen.com/persist-creation-update-timestamps-hibernate/
    @UpdateTimestamp
    private LocalDateTime date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("likeTracks")
    private UserFy userFy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("likeTracks")
    private Track track;

    public LikeTrack(UserFy userFy, Track track) {
        this.userFy = userFy;
        this.track = track;
        this.date = LocalDateTime.now();
        this.liked = true;
    }

    public LikeTrack() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserFy getUserFy() {
        return userFy;
    }

    public void setUserFy(UserFy userFy) {
        this.userFy = userFy;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
