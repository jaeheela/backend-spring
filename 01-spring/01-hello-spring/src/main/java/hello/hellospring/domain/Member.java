package hello.hellospring.domain;
//비즈니스 요구사항 정리
// => 데이터: 회원ID, 이름
// => 기능: 회원 등록, 조회
// => 아직 데이터 저장소가 선정되지 않음(가상의 시나리오)

// 일반적인 웹 애플리케이션 계층 구조
// => 컨트롤러: 웹 MVC의 컨트롤러 역할
// => 서비스: 핵심 비즈니스 로직 구현
// => 리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
// => 도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨

//클래스 의존관계
// => 아직 데이터 저장소가 선정되지 않아서, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계
// => 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가정
// => 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//회원 도메인 - 회원 객체
//JPA 엔티티 이용해 매핑하기 - JPA가 관리하는 회원 엔티티가 됨
@Entity
public class Member {
    //DB가 자동으로 숫자값을 증가하여 자동 값을 넣을 수 있도록 strategy = GenerationType.IDENTITY 로 설정, 오라클 DB는 시퀀스 사용하기도 함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name = "name") : DB의 컬럼명과 필드명이 같을 경우 생략 가능
    private String name;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*
    private Long id;//시스템의 자동증가값 이용
    private String name; //고객의 입력값 이용(Member 매개변수로 넘어올 것임)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
     */
}
