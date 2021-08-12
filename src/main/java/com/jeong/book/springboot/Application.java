package com.jeong.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @EnableJpaAuditing
 *      JPA Auditing 활성화
 *      삭제 => Test를 하기 위해서는 WebMvcTest를 사용한다. 이는 Entity가 존재하지 않는다.
 *      하지만 저 위에 어노테이션은 최소 하나의 Entity 클래스를 필요로 합니다. 그래서 제거합니다.
 *      대신, config 패키지에 새로 집어넣습니다. 이는 test에서는 볼 수 없는 패키지이므로 가능합니다.
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
