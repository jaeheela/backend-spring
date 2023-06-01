package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

//정말 단순하게 new-form 이라는 뷰의 논리 이름만 반환하면 된다.
public class MemberFormControllerV4 implements ControllerV4 {

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        //String으로만 반환하면 끝!
        return "new-form";
    }
}
