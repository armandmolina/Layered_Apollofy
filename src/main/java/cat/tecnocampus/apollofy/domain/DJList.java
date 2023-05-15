package cat.tecnocampus.apollofy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

/*
TODO 4.0
    This is the DJList. As you see, it doesn't point to any track or track fragment. (And it doesn't need to)
    Actually, you don't need to modify this class
 */
@Entity(name = "dj_list")
public class DJList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"authoredTracks"})
    private UserFy owner;

    public UserFy getOwner() {
        return owner;
    }

    public void setOwner(UserFy owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
