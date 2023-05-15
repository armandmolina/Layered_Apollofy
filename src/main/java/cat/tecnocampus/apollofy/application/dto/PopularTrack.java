package cat.tecnocampus.apollofy.application.dto;

import java.util.List;

public class PopularTrack {
    private Long trackId;
    private String title;
    private List<String> artistNames;
    private long likes;

    public PopularTrack(Long trackId, String title, Long likes) {
        this.trackId = trackId;
        this.title = title;
        this.likes = likes;
    }

    public Long getTrackId(){
        return trackId;
    }

    public String getTitle() {
        return title;
    }

    public long getLikes() {
        return likes;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public List<String> getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(List<String> artistNames) {
        this.artistNames = artistNames;
    }
}
