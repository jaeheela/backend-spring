package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * 로그를 출력하기 위한 MyLogger 클래스
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL){
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("[" + uuid + "]" + "[" + requestURL + "]" + message);
    }

    // ObjectProvider.getObject() 를 호출하시는 시점에 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리
    // => 1st. init() 호출
    // => HTTP request와 나의 uuid와 연결
    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create:" + this);
    }

    @PreDestroy
    public void close(){
        System.out.println("MyLogger.close");
        System.out.println("[" + uuid + "] request scope bean close:" + this);
    }
}
