package hello.servlet.web.frontcontroller.v2.controller;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//회원 등록 폼
public class MemberFormControllerV2 implements ControllerV2 {

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //기존 V1 코드
        //String viewPath = "/WEB-INF/views/new-form.jsp";
        //RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        //dispatcher.forward(request, response);

        //MyView 사용해 변경된 V2 코드
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
