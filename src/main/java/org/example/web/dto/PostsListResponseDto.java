package org.example.web.dto;

import lombok.Getter;
import org.example.domain.posts.Posts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private String modifiedDate;
    private int view;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss").format(entity.getModifiedDate());
        this.view = entity.getView();
    }
}
