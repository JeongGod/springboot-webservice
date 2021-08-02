package com.jeong.book.springboot.web.dto;


import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDtoTest {
    @Test
    public void 롬복_기능_테스트(){
        // Given
        String name = "test";
        int amount = 1000;

        // When
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        // then
        /**
         * assertThat
         *      검증받고 싶은 대상을 메소드 인자로 받는다.
         *      메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용가능
         * isEqualTo
         *      assertThat에 있는 값과 isEqualTo의 값을 비교해서 같을 때만 성공이다.
         */
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
