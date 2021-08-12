package com.jeong.book.springboot.web;

import com.jeong.book.springboot.config.auth.LoginUser;
import com.jeong.book.springboot.config.auth.dto.SessionUser;
import com.jeong.book.springboot.service.posts.PostsService;
import com.jeong.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    // 머스테치 스타터 덕분에 앞의 경로와 뒤의 파일 확장자는 자동으로 지정이 된다.
    // src/main/resources/templates/index.mustache => index
    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if(user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
