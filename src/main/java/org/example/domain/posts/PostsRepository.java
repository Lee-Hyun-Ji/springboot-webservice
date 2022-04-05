package org.example.domain.posts;

import org.example.web.dto.PostsListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    // SELECT * FROM Posts WHERE Title Like '%keyword%'
    Page<Posts> findByTitleContaining(String keyword, Pageable pageable);

}
