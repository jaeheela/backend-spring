package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 로거가 잘 작동하는지 확인하는 테스트용 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    // [http://localhost:8080/log-demo
    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) /*throws InterruptedException*/ {
        String requestURL = request.getRequestURL().toString();

        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestURL(requestURL);
        myLogger.log("controller test");
        //Thread.sleep(5000);
        logDemoService.logic("testId");
        return "OK";
    }
}
