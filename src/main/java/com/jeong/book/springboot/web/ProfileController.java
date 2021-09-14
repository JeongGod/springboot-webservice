package com.jeong.book.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * env.getActiveProfiles()
 *      현재 실행중인 ActiveProfile을 모두 가져옵니다.
 *      즉, real, oauth, real-db등이 활성화되어 있따면 3개가 모두 담겨져 있다.
 *      여기서 real, real1, real2는 모두 배포에 사용될 profile이라 이중 하나라도 있으면 그 값을 반환하도록 한다.
 *      real1, real2만 사용되지만, step2를 사용할수도 있으니 real도 남겨둔다.
 */
@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
