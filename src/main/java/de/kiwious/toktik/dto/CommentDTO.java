package de.kiwious.toktik.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private String authorId;
    private String content;
}

