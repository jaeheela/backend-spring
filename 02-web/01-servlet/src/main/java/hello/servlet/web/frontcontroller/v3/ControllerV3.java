package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;
//Model 추가 - v3
public interface ControllerV3 {
    //서블릿 기술 모두 제거
    ModelView process(Map<String, String> paramMap);
}
