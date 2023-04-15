package hello.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
//http://localhost:8090/test로 요청이 오면 이 서블릿이 실행
//TestServlet.service를 로그에 출력
//rseponse 객체에 test를 담아 클라이언트에게 전달 - 즉, test라고 응답
//즉, 웹 브라우저로 요청하면 이 서블릿이 실행되고 화면에 test 가 출력되어야 함
//그런데 이 서블릿을 실행하려면 톰캣 같은 웹 애플리케이션 서버(WAS)에 이 코드를 배포해야 함

//WAR 빌드하는 법
//1. 프로젝트 빌드 - 콘솔창에서 프로젝트 폴더로 이동 >> ./gradlew build
//2. 빌드폴더,라이브러리폴더로 이동 - cd build >> cd libs
//3. WAR 파일 생성 확인 -ls >> build/libs/server-0.0.1-SNAPSHOT.war
// => build.gradle 에 보면 war 플러그인이 사용된 것을 확인할 수 있다 , 이 플러그인이 war 파일을 만들어준다.
//4. 빌드한 WAR 압축 풀어 내용물 확인 - jar -xvf server-0.0.1-SNAPSHOT.war
//5. WAR 푼 결과 확인 - WEB-INF >> classes >> hello/servlet/TestServlet.class
//                   WEB-INF >> lib >> jakarta.servlet-api-6.0.0.jar
//                   index.html

//JAR
//=> 자바는 여러 클래스와 리소스를 묶어서 JAR (Java Archive)라고 하는 압축 파일을 만들 수 있음
//=> 이 파일은 JVM 위에서 직접 실행되거나 또는 다른 곳에서 사용하는 라이브러리로 제공됨
//=> 직접 실행하는 경우 main() 메서드가 필요하고, MANIFEST.MF 파일에 실행할 메인 메서드가 있는 클래스를 지정해두어야 한다.
//=> 실행 예) java -jar abc.jar
//=> Jar는 쉽게 이야기해서 클래스와 관련 리소스를 압축한 단순한 파일이다.
//=> 필요한 경우 이 파일을 직접 실행할 수도 있고, 다른 곳에서 라이브러리로 사용할 수도 있다.

//WAR
//=> WAR(Web Application Archive)라는 이름에서 알 수 있듯 WAR 파일은 웹 애플리케이션 서버(WAS)에 배포할 때 사용하는 파일이다.
//=> JAR 파일이 JVM 위에서 실행된다면, WAR는 웹 애플리케이션 서버 위에서 실행된다.
//=> HTML 같은 정적 리소스와 클래스 파일을 모두 함께 포함하기 때문에 JAR와 비교해서 구조가 더 복잡하다.
//=> 그리고 WAR 구조를 지켜야 한다.

//WAR 구조
// => WEB-INF : 폴더 하위는 자바 클래스와 라이브러리, 그리고 설정 정보가 들어가는 곳
// => WEB-INF >> classes : 실행 클래스 모음
// => WEB-INF >> lib : 라이브러리 모음
// => WEB-INF >> web.xml : 웹 서버 배치 설정 파일(생략 가능)
// => index.html : 정적 리소스 (WEB-INF 를 제외한 나머지 영역은 HTML, CSS 같은 정적 리소스가 사용되는 영역)

//WAR 배포하는 법 - 톰캣 서버에 배포하기
//1. 콘솔창에서 톰캣 서버 종료 - ./shutdown.sh
//2. 톰캣폴더/webapps 하위를 모두 삭제
//3. 빌드된 server-0.0.1-SNAPSHOT.war 복사 후 톰캣폴더/webapps 하위에 붙여넣기 (톰캣폴더/webapps/server-0.0.1-SNAPSHOT.war)
//5. 이름 변경 - 톰캣폴더/webapps/ROOT.war (ROOT 는 대문자를 사용)
//6. 톰캣 서버 실행 - ./startup.sh >> 실행하면 톰캣이 자동으로 ROOT.war의 압축을 풀어 폴더가 생성됨
//7. 실행 결과 확인
// => http://localhost:8090/index.html - 브라우저에 index.html 문서가 실행됨
// => http://localhost:8090/test - 브라우저에 test 값 찍힘
// => 진행이 잘 되지 않으면 톰캣폴더/logs/catalina.out 로그 꼭 확인

//인텔리J에서 내컴퓨터에 설치한 톰캣 실행하는 법
// => Run > >Run... >> Edit Configurations >> 플러스 버튼 >> Other >> Tomcat Server(TomEE Server 아님) >> Local
// => >> Configure... >> Tomcat Home 부분에 설치된 톰캣 폴더 선택 >> Deployment 메뉴 선택 >>
// => >> + 버튼 선택 >> 끝에 (exploded)로 끝나는 war 파일을 선택 >> Application context 박스 안에 있는 내용을 모두 지움 >> 설정한 톰캣을 선택하고 실행
// => 주의) java.net.BindException: Address already in use 오류 메시지가 로그에 보이면 앞서 실행한 톰캣 서버가 이미 8080 포트를 점유하고 있을 가능성이 높음 - shutdown.sh 를 실행해서 앞서 실행한 톰캣 서버 내리기 (잘 안되면 컴퓨터 재부팅)
// => http://localhost:8090/test 확인
/**
 * http://localhost:8090/test
 */
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse rseponse) throws ServletException, IOException {
        System.out.println("TestServlet.service");
        rseponse.getWriter().println("test");
    }
}
// hello.container.MyContainerInitV1.java를 보자