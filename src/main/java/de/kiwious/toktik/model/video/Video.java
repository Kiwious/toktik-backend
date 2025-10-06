package de.kiwious.toktik.model.video;

import de.kiwious.toktik.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "videos")
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    private String description;
    private int likes;
    private String url;
    private String s3Key;

    public List<Comment> getComments() {
        return null;
    }
}
