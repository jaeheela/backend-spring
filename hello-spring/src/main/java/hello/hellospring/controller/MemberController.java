package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// => 스프링 빈을 등록하는 2가지 방법
// 1. [컴포넌트 스캔과 자동 의존관계 설정]
// [컴포넌트 스캔 원리] : @Component 애노테이션이 있으면 스프링 빈으로 자동 등록된다. @Component 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다.
// => 참고로 helloController는 스프링이 제공하는 컨트롤러여서 스프링 빈으로 자동 등록된다. - @Controller가 있으면 자동 등록됨
// => [@Controller] [@Service] [@Repository] : 이로써 MemberController, MemberService, MemberRepository 가 스프링 컨테이너에 스프링 빈으로 등록되었다.
// => 참고로 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록한다(유일하게 하나만 등록해서 공유한다)
// => 따라서 같은 스프링 빈이면 모두 같은 인스턴스다. 설정으로 싱글톤이 아니게 설정할 수 있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용한다.
// [자동 의존관계 설정 @Autowired] : 생성자에 @Autowired 가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다.
// => 이렇게 객체 의존관계를 외부에서 넣어주는 것을 DI (Dependency Injection), 의존성 주입이라 한다.
// => 이전 테스트에서는 개발자가 직접 주입했고, 여기서는 @Autowired에 의해 스프링이 주입해준다.
// => 생성자에 @Autowired 를 사용하면 객체 생성 시점에 스프링 컨테이너에서 해당 스프링 빈을 찾아서 주입한다. 생성자가 1개만 있으면 @Autowired는 생략할 수 있다.
// 예제
// => 회원 컨트롤러가 회원서비스와 회원 리포지토리를 사용할 수 있게 의존관계 설정
// => MemberController(@Controller + @Autowired) >> MemberService(@Service + @Autowired) >> MemoryMemberRepository(@Repository)

// 2. [자바 코드로 직접 스프링 빈 등록]
// => 단, [회원 서비스]]의 @Service, @Autowired 애노테이션을 제거하고 진행한다.
// => 단, [회원 리포지토리]의 @Repository, @Autowired 애노테이션을 제거하고 진행한다.
// => 여기서는 향후 메모리 리포지토리를 다른 리포지토리로 변경할 예정이므로, 컴포넌트 스캔 방식 대신에 자바 코드로 스프링 빈을 설정하겠다.
// => 참고로 XML로 설정하는 방식도 있지만 최근에는 잘 사용하지 않으므로 생략한다.
// => DI에는 필드 주입, setter 주입, 생성자 주입 이렇게 3가지 방법이 있다. 의존관계가 실행중에 동적으로 변하는 경우는 거의 없으므로 생성자 주입을 권장한다.
// => 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다. 그리고 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.
// => 주의) @Autowired 를 통한 DI는 helloController , memberService 등과 같이 스프링이 관리하는 객체에서만 동작한다. 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않는다.
// => 스프링 컨테이너, DI 관련된 자세한 내용은 스프링 핵심 원리 강의에서 설명한다.
// 예제
// => 회원 컨트롤러가 회원서비스와 회원 리포지토리를 사용할 수 있게 의존관계 설정
// => MemberController(@Controller + @Autowired) >> SpringConfig(@Configuration - MemberService(Bean) + MemoryMemberRepository(@Bean) )

//회원 등록 컨트롤러
@Controller
public class MemberController {
    private final MemberService memberService;
    //자바 코드로 직접 스프링 빈 등록하기 위해 @Autowired 제거 - SpringConfig.java
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //회원 등록 컨트롤러 - 회원 등록 폼 HTML 요청
    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    //회원 등록 컨트롤러 - 회원 등록 기능
    @PostMapping (value = "/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/";
    }

    //회원 등록 컨트롤러 - 회원 조회 기능
    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    //회원 등록 컨트롤러 - 데이터를 전달 받을 폼 객체
    public class MemberForm {
        private String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
