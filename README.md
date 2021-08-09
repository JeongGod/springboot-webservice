# 스프링 부트를 이용한 웹 개발
## 제목 : 스프링 부트와 AWS로 혼자 구현하는 웹 서비스
## 저자 : 이동욱
## 출판사 : 프리렉
---
> [Spring과 Spring Boot의 차이](#spring과-spring-boot의-차이)   
> [DI & IOC](#di--ioc)   
> [ORM & JPA](#orm--jpa)   
> [JPA의 영속성 컨텍스트](#jpa의-영속성-컨텍스트)   
> [Entity와 Repository](#entity와-repository)   
---
# Spring과 Spring Boot의 차이
Spring이란 무엇인가.

스프링의 주요한 특징으로는 **DI(Dependency Injection)**, **IOC(Inversion Of Control)** 두 가지가 있다.

위 두가지의 특징으로 인해서 **결합도가 낮은 어플리케이션**을 개발할 수 있다.

⇒ 결합도가 낮으면 단위테스트의 용이하기 때문에 퀄리티 높은 프로그램을 개발할 수 있다.

```java
@RestController
public class MyController {
    private MyService service = new MyService();

    @RequestMapping("/welcome")
    public String welcome() {
        return service.retrieveWelcomeMessage();
    }
}
```

 위의 예제를 보자. MyController는 MyService라는 클래스에 의존한다고 설계를 했다면, 인스턴스를 얻기위해서는 다음과 같이 코드를 작성해야 한다.

 추후에 MyController에 대한 단위 테스트를 진행하기 위해, Mock객체를 생성한다면, 어떻게 MyController가 Mock객체를 이용할 수 있을까?

```java
@Component
public class MyService {
    public String retrieveWelcomeMessage(){
       return "Welcome to Innovation";
    }
}

@RestController
public class MyController {
    @Autowired
    private MyService service;

    @GetMapping("/welcome")
    public String welcome() {
        return service.retrieveWelcomeMessage();
    }
}
```

위처럼 두 개의 어노테이션으로 MyService객체의 인스턴스를 쉽게 얻을 수 있다. 이는 결합도를 낮춘 것이다.

스프링 프레임워크는 이렇게 일을 단순하게 만들기 위한 방법을 제공한다.

> @Component   
스프링의 BeanFactory라는 팩토리 패턴의 구현체에서 bean이라는 스프링프레임워크가 관리하는 객체가 있다. 해당 클래스를 그러한 bean객체로 두어 스프링 관리하에 두겠다는 어노테이션이다.

> @Autowired   
스프링 프레임워크에서 관리하는 Bean객체와 같은 타입의 객체를 찾아서 자동으로 주입해주는 것이다. 해당 객체를 Bean으로 등록하지 않으면 주입해줄 수 없다.

### 그렇다면 Spring Boot는 뭘 하는 친구냐?

Transaction Manager, Hibernate Datasource, Entity Manager, Session Factory와 같은 설정을 하는데 많은 어려움이 있다. Spring MVC를 사용하여 기본 프로젝트를 셋팅하는데 개발자에게 너무 많은 시간이 걸렸다.

⇒ 스프링 부트가 나와서 해결해줬다. 자동설정(AutoConfiguration)을 이용하였고, 모든 내부 디펜던시를 관리한다. 개발자가 해야하는건 어플리케이션을 실행할 뿐이다. 미리 설정된 스타터 프로젝트를 제공하는 것이다.

(react create app이랑 비슷한 느낌인가)

저 위에 설정들이 호환되게끔 하기 위해서 우리가 "직접" 버전을 선택해주고 설정해줘야 했다. 하지만 "SpringBoot Starter"라는 것을 도입하여 해결했다.

⇒ 개발자가 Dependency 관리와 호환버전에 대하여 관리할 필요가 없어졌다.

```java
// Web, Restful 응용프로그램
implementation('org.springframework.boot:spring-boot-starter-web')
// Spring Data JPA with Hibernate
implementation('org.springframework.boot:spring-boot-starter-data-jpa')
// Unit Testing, Integration Testing
testImplementation('org.springframework.boot:spring-boot-starter-test')
```

> 참고 : [https://sas-study.tistory.com/274](https://sas-study.tistory.com/274)
---

# DI & IOC
DI란 무엇인가. Dependency Injection의 줄임말로 의존성 주입이라고 한다.

> 의존성 
어떤 "서비스"를 호출하려는 그 "클라이언트"는 그 "서비스"가 어떻게 구성되었는지 알지 못해야 한다.
"클라이언트"는 대신 서비스 제공에 대한 책임을 외부 코드(주입자)로부터 위임한다.
"클라이언트"는 주입자 코드를 호출할 수 없다.
그 다음, 주입자는 이미 존재하거나 주입자에 의해 구성되었을 서비스를 클라이언트로 주입(전달)한다.
그러고 나서 클라이언트는 서비스를 이용한다.
이는 클라이언트가 주입자와 서비스 구성 방식 또는 사용중인 실제 서비스에 대해 알 필요가 없음을 의미한다.
"클라이언트" : 서비스의 사용 방식을 정의하고 있는 서비스의 고유한 인터페이스에 대해서만 알면 된다.
`구성의 책임`으로부터 `사용의 책임`을 구분한다. - 출처 : 위키백과

## DI 사용 X

```java
public class Coffee {...}

public class Programmer {
	private Coffee coffee = new Coffee();

	public startProgramming(){
		this.coffee.drink()
	}
}
```

여기서 Programmer객체는 Coffee객체가 필요해서 Coffee객체를 생성했다.
Programmer객체는  Coffee객체에 의존하고 있다. 라고 설명할 수 있다.

만약, Coffee객체가 아닌 Cappuccino, Americano객체를 사용하고 싶다면 해당 코드는 수정해야 한다.
결합도(coupling)이 높아지게 되어 코드의 재활용성등 문제가 많아진다.

## DI 사용

```java
public class Coffee {...}
public class Cappuccino extends Coffee {...}
public class Americano extends Coffee {...}

public class Programmer {
	private Coffee coffee;

	public Programmer(Coffee coffee){
		this.coffee = coffee
	}

	public startProgramming(){
		this.coffee.drink()
	}
}
```

이 코드를 보면 Programmer객체에 Coffee라는 객체를 "주입"해주는 것을 알 수 있다.
 이렇게 함으로서 Programme객체가 Cappuccino를 마시는 중이라면, Cappuccino객체를 Programmer에 넘겨주어 생성하면 된다. Americano를 마시고 싶다면 똑같이 하면 된다! 코드를 계속해서 재사용할 수 있게 되는 것이다.

---

여기서 부터 객체지향개발론에서 배웠던 내용을 복습해볼게요.

상속 모델들은 "is a" relationship을 가진다고 했어요. 위 예시를 살펴보면

> Coffee is a Coffee   
Cappuccino is a Coffee   
Americano is a Coffee

 그래서, Transitivity가 성립이 됩니다. 만약 Cat → Mammal, Mammal → Animal이라면, Cat → Animal이 성립된다는 말입니다.

이 얘기를 왜 했냐면 저기 Coffee coffee객체에 Cappuccino, Americano객체가 들어갈 수 있다는 설명을 하려고 했습니다. 바로 LSP(Liskov Substitution Principle)으로 인해서 
`Coffee coffee = new Americano();` 가 성립합니다.

---

DI를 사용함으로서의 장점

1. Unit Test가 용이해진다. ⇒ 의존성이 낮아지니깐
2. 코드의 재활용성이 높아진다. ⇒ 하나의 객체에 의존하지 않으니깐
3. 객체 간의 의존성(종속성)을 줄이거나 없앨 수 있다.
4. 객체 간의 결합도를 낮추면서 유연한 코드를 작성할 수 있다.

# DI 프레임워크인 Spring

Spring 프레임워크는 DI를 사용하는데 편하게 해준다. 방법은 총 3가지가 있다.

1. 생성자 주입(Constructor Injection)

```java
@Service 
public class UserServiceImpl implements UserService {   
    private UserRepository userRepository; 
    private MemberService memberService; 
    @Autowired 
    public UserServiceImpl(UserRepository userRepository, 
														MemberService memberService) { 
        this.userRepository = userRepository; 
				this.memberService = memberService; 
    } 
}

// 출처: https://mangkyu.tistory.com/125 [MangKyu's Diary]
```

생성자 주입은 생성자의 호출 시점에 "1회" 호출 되는 것이 보장된다.

그렇기 때문에 `주입받는 객체의 변화가 없거나 반드시 객체의 주입이 필요한 경우`에 강제하기 위해 사용이 가능하다.

스프링에서는 생성자가 1개만 있을 경우에는 `@Autowired` 는 생략 가능하다. 그래서 아래와 같이 변경이 가능하다.

```java
@Service 
public class UserServiceImpl implements UserService {   
    private UserRepository userRepository; 
    private MemberService memberService; 

    public UserServiceImpl(UserRepository userRepository, 
														MemberService memberService) { 
        this.userRepository = userRepository; 
				this.memberService = memberService; 
    } 
}

// 출처: https://mangkyu.tistory.com/125 [MangKyu's Diary]
```

---

2. 수정자 주입(Setter Injection)

Setter를 이용하여 의존 관계를 주입하는 방법이다. `주입받는 객체가 변할 수 있는 경우`에 사용한다.

```java
@Service 
public class UserServiceImpl implements UserService { 
    private UserRepository userRepository; 
    private MemberService memberService; 
    @Autowired 
    public void setUserRepository(UserRepository userRepository) { 
        this.userRepository = userRepository; 
    } 
    @Autowired 
    public void setMemberService(MemberService memberService) { 
        this.memberService = memberService; 
    } 
}
```

만일 `@Autowired` 로 주입할 대상이 없는 경우에는 오류가 발생한다. ⇒ 빈에 존재하지 않는 경우

주입할 대상이 없도록 하려면 `@Autowired(required = false)` 를 통해 설정이 가능하다.

---

3. 필드 주입(Field Injection)

필드에 바로 의존 관계를 주입하는 방법이다.

```java
@Service 
public class UserServiceImpl implements UserService { 
		@Autowired
    private UserRepository userRepository; 
		@Autowired
    private MemberService memberService; 
}
```

코드가 간결해져서 과거에 많이 이용된 방법이다. 하지만, 위 방법은 외부에서 변경이 불가능하다.

테스트 코드에서 Mock데이터를 사용하여 주입하게되면 안된다는 말이다.

또, 필드 주입은 DI 프레임워크(스프링같은)가 존재해야지만 사용이 가능하다. 그래서 반드시 사용을 지양해야한다. ⇒ **어플리케이션의 작동과 무관한 테스트 코드나 설정을 위해서만 사용하자.**

---

위 3가지 방법중 우리는 **생성자 주입**을 권장한다. 그 이유는 다음과 같다.

1. 객체의 불변성 확보
2. 테스트 코드의 작성
3. final 키워드 작성 및 Lombok의 결합

    위 3가지 방법중 생성자 주입만이 객체의 생성과 "동시에" 의존성 주입이 이루어진다. 그래서 final변수를 할당 할 수 있다. 나머지 방법은 객체의 생성 ⇒ 의존성 주입 함수 호출로 이루어지기에 final변수를 할당 할 수 없다.

4. 순환 참조 에러 방지

    아까 말했다시피, 객체의 생성과 "동시에" 의존성 주입이 이루어진다. 
    클라이언트 구동시, Bean에 등록하기 위해 객체를 생성하는데 이 때 순환참조를 찾으면 바로 에러를 내뱉는다. 만약 수정자 주입을 사용했다면 `해당 객체를 사용하기 위해 의존성을 주입했을 경우` 에러가 나서 처음에는 뭣도 모르다가 나중에 발견할 수도 있는 경우가 생긴다.

    이를 방지하기 위해서 생성자 참조를 사용해야 한다.

> 참고   
[https://velog.io/@wlsdud2194/what-is-di](https://velog.io/@wlsdud2194/what-is-di)   
[https://mangkyu.tistory.com/125](https://mangkyu.tistory.com/125)   
[https://keichee.tistory.com/446](https://keichee.tistory.com/446)

# IOC

위에 DI를 완벽하게 이해했다면, IOC를 알아보자. DI와 IOC는 아주 밀접한 관계를 맺고 있기 때문에 꼭 위에 내용을 이해해야만 한다.

제어의 역전. 프로그램을 제어하는게 원래는 누군지 생각해보자. 바로 프로그래밍을 하는 개발자들이다.

 이러한 프로그램의 모든 것들을 개발자가 관리해야한다면? 얼마나 힘들까. 사용자들이 이거하고싶어요. 하면 개발자는 그거에 맞춰서 다시 프로그래밍.. 다시 프로그래밍을 해야한다.

이러한 점을 타파하기 위해 제어권을 제 3자에게 위임하는 것이다. 이것이 바로 IOC다.

## 컨테이너

스프링에서는 컨테이너라는 용어가 등장한다. Servlet 컨테이너, EJB 컨테이너등.. 많은 컨테이너들이 있다.

이 컨테이너들은 뭐하는 녀석들인가? 바로 **인스턴스의 생명주기를 관리, 생성된 인스턴스들에게 추가적인 기능을 제공하도록 하는 것**이라고 한다.

 즉, 당신이 작성한 처리과정을 위임받은 독립적인 존재라고 생각하면 된다. 알아서 코드를 참조하여 객체의 생성과 소멸을 컨트롤해주는 것이다.

## 스프링 컨테이너의 종류

### BeanFactory

객체를 생성하고, DI를 처리해주는 기능만을 제공하는 것이다. Bean 등록,생성,조회,반환을 관리한다.
빈 자체가 필요하게 되기 전까지는 인스턴스화를 하지 않는다.
`getBean()`메서드로 Bean을 조회할 수 있지만 잘 사용하지 않는다고 한다.

- 처음으로 getBean()이 호출된 시점에서야 해당 빈을 생성한다. (Lazy Loading)

### ApplicationContext

스프링이 제공하는 다양한 부가 기능을 담고있다. BeanFactory와 유사하지만 좀 더 많은 기능을 제공한다.

BeanFactory에서 추가되는 기능

1. 국제화가 지원되는 텍스트 메시지를 관리해 준다.
2. 이미지같은 파일 자원을 로드할 수 있는 포괄적인 방법을 알려준다.
3. 리스너로 등록된 빈에게 이벤트 발생을 알려준다.
- 컨텍스트 초기화 시점에 모든 싱글톤 빈을 미리 로드한 후 애플리케이션 가동 후에는 Bean을 지연없이 얻을 수 있다. ⇒ 미리 Bean을 생성해 놓아 빈이 필요할 때 즉시 사용할 수 있도록 보장한다.

정리하자면, 컨테이너는 우리의 코드를 읽어서 객체에 대한 정보를 알아둔다. 그리고 이용자가 호출을 하게 된다면 그 때 그에 맞는 객체를 할당해주는 것이다.

 이로 인해 프로그래머는 `프레임워크에 필요한 부품을 개발,조립하는 방식의 개발`만 하면된다. 나머지는 알아서 컨테이너에서 처리해주니깐 말이다.

> 참고   
[https://biggwang.github.io/2019/08/31/Spring/IoC, DI란 무엇일까/](https://biggwang.github.io/2019/08/31/Spring/IoC,%20DI%EB%9E%80%20%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C/)   
[https://jjunii486.tistory.com/84](https://jjunii486.tistory.com/84)

# ORM & JPA
### ORM부터 알아보자.

- ORM (Object-Relational Mapping)으로 객체와 관계형 데이터베이스 매핑, 객체와 DB의 테이블이 매핑을 이루는 것이다.
- SQL Query가 아닌 직관적인 코드(메서드)로서 데이터를 조작할 수 있다.
- 자바 객체와 쿼리를 분리하여 복잡도를 줄이고, 트랜잭션 처리나 기타 데이터베이스 관렺갖업들을 좀 더 편리하게 처리할 수 있는 방법이다.

### JPA는 뭐냐.

- Java Persistence API로 자바 ORM기술에 대한 API표준 명세이다.
- ORM을 사용하기 위한 인터페이스를 모아둔 것이다.
- 자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스다.
- "인터페이스"이기 때문에 JPA를 사용하기 위해서는 JPA를 구현한 Hibernate, EclipseLink, DataNucleus와 같은 ORM 프레임워크를 사용해야 한다.

⭐️ JPA의 특징으로는 **ORM**, **영속성 컨텍스트**다.이 2가지가 주요 특징이다!

### JPA의 장점

1. 생산성이 뛰어나고 유지보수가 용이하다. 객체 중심 설계에 더 집중할 수 있기 때문이다.
2. DBMS에 대한 종속성이 줄어든다.
⇒ DB쿼리문을 알아서 해주니, DB만 변경하면 되는 것이다. 많은 작업을 하지 않아도 된다.

### JPA의 단점

1. JPA를 학습하려면 많은 노력이 든다.
2. 복잡한 쿼리를 사용할 경우 불리하다.
⇒ 하나의 테이블에서는 이 정보, 다른 테이블에서는 이 정보를 가져와 하나의 데이터 객체로 만들기가 어렵다. SQL문에서는 join함수로 쿼리 한 줄이면 끝난다.
3. 잘못사용하면 성능이 떨어질 수도 있다.

> 출처
[https://goddaehee.tistory.com/209](https://goddaehee.tistory.com/209)

# JPA의 영속성 컨텍스트
Jpa를 설명하면서 가장 중요한 특징은 2가지가 있다.

바로 ORM과 영속성 컨텍스트다. ORM은 방금 알아봤으니 영속성 컨텍스트에 대해서 알아보자.

여기서 [트랜잭션](https://mommoo.tistory.com/92)이라는 용어가 등장한다.

JPA는 스레드가 하나 생성될 떄 Entity Manger Factory에서 EntityManger를 생성한다.
EntityManger는 내부적으로 DB 커넥션 풀을 사용하여 DB에 붙는다.

## 영속성 컨텍스트란

**엔티티를 영구 저장하는 환경**이라고 한다. EntityManger를 통해 영속성 컨텍스트에 접근한다.

- 같은 트랜잭션내에 EntityManger가 여러개 생긴다면 모두 하나의 "영속성 컨텍스트"를 바라본다.

트랜잭션이 시작한 후에 객체(Entity)를 생성하게 되면, 해당 객체(Entity)는 "영속성 컨텍스트"에 저장 및 관리가 이루어진다. 이 때 DB에 저장이 되는 상태는 아니다!!! 쿼리가 날라가지 않는다!!! 그냥 "영속성 컨텍스트"에 저장이 이루어져 있을 뿐이다.

트랜잭션이 끝나면 영속성 컨텍스트에 저장되어있던 변경사항들이 쿼리로 변환되어 DB에 날라간다. 자세한 내용은 밑에 말한다.

### 영속성 컨텍스트의 이점

- 영속성 컨텍스트 내부에는 1차 캐시가 존재한다.
- Entity를 영속성 컨텍스트에 저장하면 1차 캐시에 저장이 된다. 
(key, value)쌍으로 key = PK, value = Entity
- 1차 캐시로 DB조회가 이루어질 때 Entity가 1차 캐시에 존재한다면 굳이 DB까지 가지 않아도 바로 객체를 찾아올 수 있다.
- 만약 존재하지 않으면 DB에서 꺼내와 1차 캐시에 저장한다.
- 그렇게 되면 DB에 쿼리를 직접 날리지 않고도 객체를 찾아올 수 있다.
- 동일성을 보장한다. 만일 member1을 2번 조회한다 한들, 1차 캐시에서 참조하는 것이기 때문에 같은 주소값을 지닌다!! 이건 DB는 다른지 모르겠네

이렇게 영속성 컨텍스트에 넣으면 좋은 점은 바로바로 DB에 쿼리를 날리지 않고 한 번에 날린다는 점이다.

`쓰기 지연 SQL저장소`라는 곳에 쿼리들을 넣어 놓고, 트랜잭션이 끝나면 한꺼번에 쿼리를 날린다.

⭐️ 만약 Entity에서 수정이 일어났다면?

### Dirty Checking

더티 체킹을 이용한다. 변경 감지라는 것이다. 위에 1차 캐시에 "스냅샷 필드"도 저장되는데 트랜잭션을 `commit()` or `flush()`가 일어난다면 "Entity"와 "스냅샷"을 비교하여 변경사항이 있으면 알아서 SQL 쿼리문을 날려준다.

commit()시에는 flush()가 자동 호출된다.

flush()는 DB에게 위에서 저장된 `쓰기 지연 SQL저장소`에 있는 쿼리문을 날리는 역할이다.
영속성 컨텍스트를 비우는 역할은 하지 않는다. 

> 출처
[https://ict-nroo.tistory.com/130](https://ict-nroo.tistory.com/130)


# Entity와 Repository
## Entity

`@Entity`를 클래스에 어노테이션으로 붙이게 되면 JPA가 관리하는 클래스이고, 테이블과 매핑하게 된다.

`@id`를 사용하여 PK를 설정한다.

 `@GeneratedValue`를 사용하여 기본값을 설정할 수 있다. 다양한 옵션들이 존재한다.

## Repository

JPA처리를 담당하는 Repository는 기본적으로 4가지가 존재한다. (T : Entity의 타입클래스, ID: PK값의 type)

1. Repository<T, ID>
2. CrudRepository<T, ID>
3. PagingAndSortingRepository<T, ID>
4. JpaRepository<T, ID>

```java
package com.god.bo.jpaTest.repository; 
import com.god.bo.jpaTest.vo.MemberVo; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository; 
import java.util.List; 

@Repository 
public interface MemberRepository extends JpaRepository<MemberVo, Long> { 
    // 비워있어도 잘 작동함. 
    // long 이 아니라 Long으로 작성. ex) int => Integer 같이 primitive형식 사용못함 

    // findBy뒤에 컬럼명을 붙여주면 이를 이용한 검색이 가능하다 
    public List<MemberVo> findById(String id); 
    public List<MemberVo> findByName(String name); 

    //like검색도 가능 
    public List<MemberVo> findByNameLike(String keyword); 
}
```

위는 JpaRepository를 사용한 것이다.

`findBy...()`메소드를 이용하여 다양한 쿼리 처리가 가능하다.

> 출처
[https://goddaehee.tistory.com/209](https://goddaehee.tistory.com/209)
