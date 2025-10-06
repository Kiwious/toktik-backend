package de.kiwious.toktik.model.video;

import de.kiwious.toktik.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private String content;
    private int likes;
}
