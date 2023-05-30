package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//View 분리 - v2
public interface ControllerV2 {
    //My View 반환
    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
