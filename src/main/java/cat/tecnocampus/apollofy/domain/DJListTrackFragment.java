package cat.tecnocampus.apollofy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/*
TODO 4.1 This class represents the association between the DJList and tracks. It carries the beginning and end
    of the track fragment associated to the DJList.
    See that it points to a track and a DJlist
    You need to add all the necessary JPA annotations
 */
@Entity
public class DJListTrackFragment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startTimeMillis;

    private Long endTimeMillis;

    @ManyToOne(optional = false)
    @NotNull
    private Track track;

    @ManyToOne(optional = false)
    @NotNull
    private DJList djList;

    public DJListTrackFragment() {
    }

    public DJListTrackFragment(Long startTimeMillis, Long endTimeMillis, Track track, DJList djList) {
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
        this.track = track;
        this.djList = djList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(Long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public Long getEndTimeMillis() {
        return endTimeMillis;
    }

    public void setEndTimeMillis(Long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public DJList getPlaylist() {
        return djList;
    }

    public void setPlaylist(DJList djList) {
        this.djList = djList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DJListTrackFragment that = (DJListTrackFragment) o;
        return Objects.equals(id, that.id) && Objects.equals(startTimeMillis, that.startTimeMillis)
                && Objects.equals(endTimeMillis, that.endTimeMillis) && Objects.equals(track, that.track) && Objects.equals(djList, that.djList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTimeMillis, endTimeMillis, track, djList);
    }

    @Override
    public String toString() {
        return "PlaylistTrack{" +
                "id=" + id +
                ", startTimeMillis=" + startTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                ", track=" + track +
                ", DJList=" + djList +
                '}';
    }
}
