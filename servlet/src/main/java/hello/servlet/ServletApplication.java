package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
//step1.
// 스프링 부트 환경에서 서블릿 환경 구성하기 - hello.servlet.ServletApplication.java
// => 서블릿은 톰캣 같은 웹 애플리케이션 서버를 직접 설치하고, 그 위에 서블릿 코드를 클래스 파일로 빌드해서 올린 다음, 톰캣 서버를 실행하면 된다.
// => 하지만 이 과정은 매우 번거롭다.
// => 스프링 부트는 톰캣 서버를 내장하고 있으므로, 톰캣 서버 설치 없이 편리하게 서블릿 코드를 실행할 수 있다.

// => 스프링 부트 서블릿 환경 구성하기
//@ServletComponentScan : 스프링 부트가 서블릿을 직접 등록해서 사용할 수 있도록 하는 어노테이션 - 서블릿 자동 등록
@ServletComponentScan
@SpringBootApplication
public class ServletApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}

}
