package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;
//회원 등록 폼
public class MemberFormControllerV3 implements ControllerV3 {

    // ModelView 를 생성할 때 new-form 이라는 view의 논리적인 이름을 지정한다.
    // 실제 물리적인 이름은 프론트 컨트롤러에서 처리한다.
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}
