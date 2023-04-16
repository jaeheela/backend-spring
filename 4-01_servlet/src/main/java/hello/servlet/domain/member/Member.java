package hello.servlet.domain.member;

import lombok.Getter;
import lombok.Setter;
//회원 관리 웹 애플리케이션 요구사항

//회원 정보 (회원 도메인 모델 - Member.java)
// => 이름: username
// => 나이: age

//기능 요구사항 (회원 저장소 - MemberRepository.java)
// => 회원 저장
// => 회원 목록 조회

//회원 도메인 모델
@Getter @Setter
public class Member {

    private Long id;
    private String username;
    private int age;

    public Member() {
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
