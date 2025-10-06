package de.kiwious.toktik.model.video;

import de.kiwious.toktik.model.user.User;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "comments")
public class Comment implements Serializable {
    @Id
    private String id;

    @CreatedDate
    private Date creationDate;

    private String videoId;
    private User author;
    private String content;
    private int likes;
}
