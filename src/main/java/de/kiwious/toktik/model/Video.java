package de.kiwious.toktik.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Document(collection = "videos")
@Getter @Setter @NoArgsConstructor
public class Video implements Serializable {
    @Id
    private String id;

    @CreatedDate
    private Date creationDate;

    private User author;
    private String description;
    private int likes;
    private List<String> comments;
    private String url;
}
