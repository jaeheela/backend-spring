package hello.core.member;

//비즈니스 요구사항과 설계 - 회원
//=> 회원을 가입하고 조회할 수 있다.
//=> 회원은 일반과 VIP 두 가지 등급이 있다.
//=> 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동할 수 있다. (미확정)

//주문과 할인 정책
//=> 회원은 상품을 주문할 수 있다.
//=> 회원 등급에 따라 할인 정책을 적용할 수 있다.
//=> 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용해달라. (나중에 변경 될 수 있다.)
//=> 할인 정책은 변경 가능성이 높다. 회사의 기본 할인 정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수 도 있다. (미확정)

// => 요구사항을 보면 회원 데이터, 할인 정책 같은 부분은 지금 결정하기 어려운 부분이다.
// => 그렇다고 이런 정책이 결정될 때 까지 개발을 무기한 기다릴 수 도 없다. 우리는 앞에서 배운 객체 지향 설계 방법이 있지 않은가!
// => 인터페이스를 만들고 구현체를 언제든지 갈아끼울 수 있도록 설계하면 된다. 그럼 시작해보자.
// => 참고로 프로젝트 환경설정을 편리하게 하려고 스프링 부트를 사용한 것이다. 지금은 스프링 없는 순수한 자바로만 개발을 진행한다는 점을 꼭 기억하자! 스프링 관련은 한참 뒤에 등장한다.

//회원 도메인 설계의 문제점
// => 이 코드의 설계상 문제점은 무엇일까? 다른 저장소로 변경할 때 OCP 원칙을 잘 준수할까? DIP를 잘 지키고 있을까?
// => 의존관계가 인터페이스 뿐만 아니라 구현까지 모두 의존하는 문제점이 있음 , 주문까지 만들고나서 문제점과 해결 방안을 설명

//그림 - 클래스 다이어그램
// => 객체의 생성과 연결은 AppConfig 가 담당한다.
// => DIP 완성: MemberServiceImpl 은 MemberRepository 인 추상에만 의존하면 된다. 이제 구체 클래스를 몰라도 된다.
// => 관심사의 분리: 객체를 생성하고 연결하는 역할과 실행하는 역할이 명확히 분리되었다.
//그림 - 회원 객체 인스턴스 다이어그램
// => appConfig 객체는 memoryMemberRepository 객체를 생성하고 그 참조값을 memberServiceImpl 을 생성하면서 생성자로 전달한다.
// => 클라이언트인 memberServiceImpl 입장에서 보면 의존관계를 마치 외부에서 주입해주는 것 같다고 해서 DI(Dependency Injection) 우리말로 의존관계 주입 또는 의존성 주입이라 한다.

// 정리
// => AppConfig를 통해서 관심사를 확실하게 분리했다.
// => 배역, 배우를 생각해보자.
// => AppConfig는 공연 기획자다. AppConfig는 구체 클래스를 선택한다. 배역에 맞는 담당 배우를 선택한다. 애플리케이션이 어떻게 동작해야 할지 전체 구성을 책임진다.
// => 이제 각 배우들은 담당 기능을 실행하는 책임만 지면 된다. OrderServiceImpl 은 기능을 실행하는 책임만 지면 된다.

//회원 도메인 개발 - 회원 엔티티
public class Member {
    private Long id;
    private String name;
    private Grade grade;


    //단축키 : ctrl + n
    public Member(Long id, String name, Grade grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    //단축키 : ctrl + n
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

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
