package cat.tecnocampus.apollofy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class UserFy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String secondName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NaturalId
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "artists")
    @JsonIgnoreProperties("artists")
    private List<Track> authoredTracks = new ArrayList<>();

    @OneToMany(mappedBy = "origin", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("origin")
    private Set<FanOf> fanOf = new HashSet<>();

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    protected void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Track> getAuthoredTracks() {
        return authoredTracks;
    }

    protected void setAuthoredTracks(List<Track> authoredTracks) {
        this.authoredTracks = authoredTracks;
    }

    public void addAuthoredTrack(Track authoredTrack) {
        this.authoredTracks.add(authoredTrack);
        authoredTrack.addArtist(this);
    }

    public void addNewFan(UserFy destination) {
        fanOf.add(new FanOf(this, destination));
    }

    public Set<FanOf> getFanOf() {
        return fanOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFy)) return false;

        UserFy userFy = (UserFy) o;

        if (id != null ? !id.equals(userFy.id) : userFy.id != null) return false;
        return email.equals(userFy.email);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result +  email.hashCode();
        return result;
    }
}
