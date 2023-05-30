package hello.servlet.web.springmvc.v2;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//스프링 MVC - 컨트롤러 통합
// => @RequestMapping 을 잘 보면 클래스 단위가 아니라 메서드 단위에 적용된 것을 확인할 수 있다.
// => 따라서 컨트롤러 클래스를 유연하게 하나로 통합할 수 있다.

/**
 * 클래스 단위->메서드 단위
 * @RequestMapping 클래스 레벨과 메서드 레벨 조합
 *
 * <조합 결과>
 * 클래스 레벨 @RequestMapping("/springmvc/v2/members")
 * 메서드 레벨 @RequestMapping("/new-form") /springmvc/v2/members/new-form
 * 메서드 레벨 @RequestMapping("/save") /springmvc/v2/members/save
 * 메서드 레벨 @RequestMapping /springmvc/v2/members
 */
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    // => 등록 실행 : http://localhost:8080/springmvc/v2/members/new-form
    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }

    // => 목록 실행 : http://localhost:8080/springmvc/v2/members
    @RequestMapping
    public ModelAndView members() {

        List<Member> members = memberRepository.findAll();

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }

}
