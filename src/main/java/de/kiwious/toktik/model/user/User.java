package de.kiwious.toktik.model.user;

import de.kiwious.toktik.model.video.Video;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String discordId;

    @CreationTimestamp
    private Date creationDate;

    private String handle;
    private String displayName;
    private String imageUrl;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Video> videos = new ArrayList<>();

    /*private List<User> followers;
    private List<User> following; */
}
