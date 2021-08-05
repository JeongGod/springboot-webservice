package com.jeong.book.springboot.domain.posts;

import com.jeong.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Getter
 *      클래스 내 모든 필드의 Getter 메소드를 자동 생성
 *
 * NoArgsConstructor
 *      기본 생성자 자동 추가
 *      public Posts() {}와 같은 효과이다.
 *
 * Builder
 *      해당 클래스의 빌더 패턴 클래스를 생성
 *      생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함.
 *
 * Entity
 *      테이블과 링크될 클래스임을 나타낸다.
 *      기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍으로 테이블 이름을 매칭합니다.
 *      ex) CamelCase.java = camel_case table
 *      !! 절대 Setter메소드를 만들지 않습니다.
 *      값 변경이 필요하면 그 목적과 의도를 나타내는 메소드를 추가해야한다.
 * Id
 *      PK필드
 *      tip : 주민등록번호, 복합키등은 유니크 키로 별도로 추가하는 것을 추천합니다.
 *      나중에 복잡해집니다.
 *
 * GeneratedValue
 *      PK의 생성 규칙이다.
 *      GenerationType.IDENTITY == auto_increment
 *
 * Column
 *      테이블의 칼럼을 나타낸다. 굳이 선언하지 않아도 해당 클래스의 필드는 모두 칼럼이 된다.
 *      사용하는 이유는, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용한다.
 *      사이즈변경이나 타입을 변경하고 싶을때 사용한다.
 */
@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    /**
     * 데이터베이스에 쿼리를 날리지 않고 바로 가능하다.
     * 그 이유는 JPA의 영속성 컨텍스트때문이다.
     * 양속성 컨텍스트란, 엔티티를 영구 저장하는 환경이다.
     * JPA의 핵심 내용은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈린다.
     * JPA의 엔티티 매니저가 활성화된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태
     * 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영합니다.
     * Update쿼리를 날릴 필요가 없다는 뜻이다. => 더티 체킹이라고 한다.
     */
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}
