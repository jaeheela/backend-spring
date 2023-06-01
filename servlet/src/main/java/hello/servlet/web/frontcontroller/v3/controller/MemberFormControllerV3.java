package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;
//회원 등록 폼
public class MemberFormControllerV3 implements ControllerV3 {

    // view의 논리적인 이름 지정 : new-form - 실제 물리적인 이름은 프론트 컨트롤러에서 처리
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}
