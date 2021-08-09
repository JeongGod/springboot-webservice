package com.jeong.book.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // 머스테치 스타터 덕분에 앞의 경로와 뒤의 파일 확장자는 자동으로 지정이 된다.
    // src/main/resources/templates/index.mustache => index

    @GetMapping("/")
    public String index() {
        return "";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
}
