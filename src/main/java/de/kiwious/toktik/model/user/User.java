package de.kiwious.toktik.model.user;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true)
    private String discordId;

    @CreatedDate
    private Date creationDate;

    private String handle;
    private String displayName;
    private String imageUrl;
    private List<String> watchedVideos;
    private List<String> likedVideos;
    private String ip;

    /*private List<User> followers;
    private List<User> following; */
}
