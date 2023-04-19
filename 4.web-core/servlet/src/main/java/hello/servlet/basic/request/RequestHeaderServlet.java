package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

//HttpServletRequest 객체의 개요
// => HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다.
// => 서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다.
// => 그리고 그 결과를 HttpServletRequest 객체에 담아서 제공한다.
// => HttpServletRequest를 사용하면 다음과 같은 HTTP 요청 메시지를 편리하게 조회할 수 있다.

//HttpServletRequest 역할
// => HTTP 요청 메시지 생성 : START LINE 생성 헤더 생성, 바디 생성 / [START LINE - HTTP 메소드 , URL , 쿼리 스트링 , 스키마, 프로토콜 헤더] , [헤더 - 조회] , [바디 - form 파라미터 형식 조회 , message body 데이터 직접 조회]
// [START LINE] : POST /save HTTP/1.1
// [헤더] : Host: localhost:8080
//       : Content-Type: application/x-www-form-urlencoded
// [바디] : username=kim&age=20
// => 임시 저장소 기능 : 해당 HTTP 요청이 시작부터 끝날 때 까지 유지됨 - [저장] : request.setAttribute(name, value) / [조회] : request.getAttribute(name)
// => 세션 관리 기능 : request.getSession(create: true)

//중요
//HttpServletRequest, HttpServletResponse를 사용할 때 가장 중요한 점은 이 객체들이 HTTP 요청 메시지, HTTP 응답 메시지를 편리하게 사용하도록 도와주는 객체라는 점이다.
//따라서 이 기능에 대해서 깊이있는 이해를 하려면 HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 한다.

//HttpServletRequest가 제공하는 기본 기능들을 출력하는 서블릿
//http://localhost:8080/request-header?username=hello
@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);
    }
    //[start line 정보]
    private void printStartLine(HttpServletRequest request) {
        System.out.println("--- REQUEST-LINE - start ---");
        System.out.println("request.getMethod() = " + request.getMethod()); //GET
        System.out.println("request.getProtocal() = " + request.getProtocol()); //HTTP/1.1
        System.out.println("request.getScheme() = " + request.getScheme()); //http
        System.out.println("request.getRequestURL() = " + request.getRequestURL());// http://localhost:8080/request-header
        System.out.println("request.getRequestURI() = " + request.getRequestURI());// /request-header
        System.out.println("request.getQueryString() = " + request.getQueryString());//null -   .. username=hi
        System.out.println("request.isSecure() = " + request.isSecure()); //https 사용 유무 - false
        System.out.println("--- REQUEST-LINE - end ---");
        System.out.println();
        //--- REQUEST-LINE - start ---
        //request.getMethod() = GET
        //request.getProtocal() = HTTP/1.1
        //request.getScheme() = http
        //request.getRequestURL() = http://localhost:8080/request-header
        //request.getRequestURI() = /request-header
        //request.getQueryString() = null
        //request.isSecure() = false
        //--- REQUEST-LINE - end ---
    }

    //[Header 모든 정보]
    private void printHeaders(HttpServletRequest request) {
        System.out.println("--- Headers - start ---");
        /*
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + headerName);
        }
        */
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> System.out.println(headerName + ": " + headerName));
        System.out.println("--- Headers - end ---");
        System.out.println();
        //--- Headers - start ---
        //host: host
        //connection: connection
        //sec-ch-ua: sec-ch-ua
        //sec-ch-ua-mobile: sec-ch-ua-mobile
        //sec-ch-ua-platform: sec-ch-ua-platform
        //upgrade-insecure-requests: upgrade-insecure-requests
        //user-agent: user-agent
        //accept: accept
        //sec-fetch-site: sec-fetch-site
        //sec-fetch-mode: sec-fetch-mode
        //sec-fetch-user: sec-fetch-user
        //sec-fetch-dest: sec-fetch-dest
        //accept-encoding: accept-encoding
        //accept-language: accept-language
        //cookie: cookie
        //--- Headers - end ---
    }
    private void printHeaderUtils(HttpServletRequest request) {
        System.out.println("--- Header 편의 조회 start ---");
        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " + request.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " + request.getServerPort()); //Host 헤더
        System.out.println("[Accept-Language 편의 조회]");
        request.getLocales().asIterator().forEachRemaining(locale -> System.out.println("locale = " + locale));
        System.out.println("request.getLocale() = " + request.getLocale());
        System.out.println("[cookie 편의 조회]");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("request.getContentLength() = " + request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());
        System.out.println("--- Header 편의 조회 end ---");
        System.out.println();
        //--- Header 편의 조회 start ---
        //[Host 편의 조회]
        //request.getServerName() = localhost
        //request.getServerPort() = 8080
        //[Accept-Language 편의 조회]
        //locale = ko_KR
        //locale = ko
        //locale = en_US
        //locale = en
        //request.getLocale() = ko_KR
        //[cookie 편의 조회]
        //Idea-4bbb167b: a36200a3-ec12-4380-838b-e9f6e828ed89
        //Idea-3a450657: 3afb1f52-af87-447c-ad3d-33520aa3acbf
        //JSESSIONID: 2BF9181E9170D78740A4AA9D74BF8C2E
        //[Content 편의 조회]
        //request.getContentType() = null
        //request.getContentLength() = -1
        //request.getCharacterEncoding() = UTF-8
        //--- Header 편의 조회 end ---
    }

    //기타 정보
    private void printEtc(HttpServletRequest request) {
        System.out.println("--- 기타 조회 start ---");
        System.out.println("[Remote 정보]");
        System.out.println("request.getRemoteHost() = " + request.getRemoteHost());
        System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
        System.out.println("request.getRemotePort() = " + request.getRemotePort());
        System.out.println();
        System.out.println("[Local 정보]");
        System.out.println("request.getLocalName() = " + request.getLocalName());
        System.out.println("request.getLocalAddr() = " + request.getLocalAddr());
        System.out.println("request.getLocalPort() = " + request.getLocalPort());
        System.out.println("--- 기타 조회 end ---");
        System.out.println();
        //--- 기타 조회 start ---
        //[Remote 정보]
        //request.getRemoteHost() = 0:0:0:0:0:0:0:1
        //request.getRemoteAddr() = 0:0:0:0:0:0:0:1
        //request.getRemotePort() = 64481
        //[Local 정보]
        //request.getLocalName() = localhost
        //request.getLocalAddr() = 0:0:0:0:0:0:0:1
        //request.getLocalPort() = 8080
        //--- 기타 조회 end ---
    }
}

//참고
//로컬에서 테스트하면 IPv6 정보가 나오는데, IPv4 정보를 보고 싶으면 다음 옵션을 VM options에 넣어주면 된다. - [-Djava.net.preferIPv4Stack=true]
//지금까지 HttpServletRequest를 통해서 HTTP 메시지의 start-line, header 정보 조회 방법을 이해했다.
//이제 본격적으로 HTTP 요청 데이터를 어떻게 조회하는지 알아보자. - hello.request.RequestParamServlet.java