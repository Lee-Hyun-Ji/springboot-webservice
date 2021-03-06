package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.config.auth.LoginUser;
import org.example.config.auth.dto.SessionUser;
import org.example.service.posts.PostsService;
import org.example.web.dto.PostsListResponseDto;
import org.example.web.dto.PostsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(sort = "id", size = 10, direction = Sort.Direction.DESC)Pageable pageable,
                        @LoginUser SessionUser user) {
        Page<PostsListResponseDto> pageList = postsService.pageList(pageable);
        model.addAttribute("posts", pageList);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasPrev", pageList.hasPrevious());
        model.addAttribute("hasNext", pageList.hasNext());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index"; // = src/main/resources/templates/"index".mustache
    }

    /* 게시글 검색하기 */
    @GetMapping("/posts/search")
    public String search(String keyword, Model model,
                         @PageableDefault(sort = "id", size = 10, direction = Sort.Direction.DESC)Pageable pageable,
                         @LoginUser SessionUser user) {
        Page<PostsListResponseDto> searchList = postsService.search(keyword, pageable);
        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasPrev", searchList.hasPrevious());
        model.addAttribute("hasNext", searchList.hasNext());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "posts-search";
    }

    /* 게시글 상세 보기 */
    @GetMapping("/posts/read/{id}")
    public String read(@PathVariable Long id,  @LoginUser SessionUser user, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        //게시글 작성자 본인 확인 -> 실명으로 작성하도록 고정
        if(dto.getAuthor().equals(user.getName())){
            model.addAttribute("isAuthor", true);
        }
        //view++
        postsService.updateView(id);
        model.addAttribute("posts", dto);

        return "posts-read";
    }

    /* 게시글 저장하기 */
    @GetMapping("/posts/save")
    public String postsSave(@LoginUser SessionUser user, Model model) {
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "posts-save"; // = src/main/resources/templates/"posts-save".mustache
    }

    /* 게시글 수정하기 */
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update"; // = src/main/resources/templates/"posts-update".mustache
    }
}
