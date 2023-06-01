package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//스프링 MVC - 실용적인 방식
// => MVC 프레임워크 만들기에서 v3은 ModelView를 개발자가 직접 생성해서 반환했기 때문에, 불편했던 기억이 날 것이다.
// => 물론 v4를 만들면서 실용적으로 개선한 기억도 날 것이다.
// => 스프링 MVC는 개발자가 편리하게 개발할 수 있도록 수 많은 편의 기능을 제공한다.
// => 실무에서는 지금부터 설명하는 방식을 주로 사용한다.

// Model 파라미터
// => save() , members() 를 보면 Model을 파라미터로 받는 것을 확인할 수 있다.
// => 스프링 MVC도 이런 편의 기능을 제공한다.

// ViewName 직접 반환
// => 뷰의 논리 이름을 반환할 수 있다.

// @RequestParam 사용
// => 스프링은 HTTP 요청 파라미터를 @RequestParam 으로 받을 수 있다.
// => @RequestParam("username") 은 request.getParameter("username") 와 거의 같은 코드라 생각하면 된다.
// => 물론 GET 쿼리 파라미터, POST Form 방식을 모두 지원한다.

// @RequestMapping : @GetMapping, @PostMapping
// => @RequestMapping 은 URL만 매칭하는 것이 아니라, HTTP Method도 함께 구분할 수 있다.
// => 예를 들어서 URL이 /new-form 이고, HTTP Method가 GET인 경우를 모두 만족하는 매핑을 하려면 다음과 같이 처리하면 된다.
// => @RequestMapping(value = "/new-form", method = RequestMethod.GET)
// => 이것을 @GetMapping , @PostMapping 으로 더 편리하게 사용할 수 있다.
// => 참고로 Get, Post, Put, Delete, Patch 모두 애노테이션이 준비되어 있다.
// => @GetMapping 코드를 열어서 @RequestMapping 애노테이션을 내부에 가지고 있는 모습을 확인하자.
/**
 * v3
 * Model 도입
 * ViewName 직접 반환
 * @RequestParam 사용
 * @RequestMapping -> @GetMapping, @PostMapping
 * */

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    // => 등록 실행 : http://localhost:8080/springmvc/v3/members/new-form
    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }

    // => 목록 실행 : http://localhost:8080/springmvc/v3/members
    @GetMapping
    public String members(Model model) {

        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);
        return "members";
    }
}
