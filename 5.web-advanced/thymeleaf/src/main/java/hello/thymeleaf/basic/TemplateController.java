package hello.thymeleaf.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/template")
public class TemplateController {

    //템플릿 조각
    // => 웹 페이지를 개발할 때는 공통 영역이 많이 있다. 예를 들어서 상단 영역이나 하단 영역, 좌측 카테고리 등등 여러 페이지에서 함께 사용하는 영역들이 있다.
    // => 이런 부분을 코드를 복사해서 사용한다면 변경시 여러 페이지를 다 수정해야하므로 상당히 비효율적이다.
    // => 타임리프는 이런 문제를 해결하기 위해 템플릿 조각과 레이아웃 기능을 지원한다. 템플릿 조각과 레이아웃 부분은 직접 실행해보아야 이해된다.
    //http://localhost:8080/template/fragment
    @GetMapping("/fragment")
    public String template() {
        return "/template/fragment/fragmentMain";
    }

    //템플릿 레이아웃1
    // => 이전에는 일부 코드 조각을 가지고와서 사용했다면, 이번에는 개념을 더 확장해서 코드 조각을 레이아웃에 넘겨서 사용하는 방법에 대해서 알아보자.
    // => 예를 들어서 <head> 에 공통으로 사용하는 css , javascript 같은 정보들이 있는데,
    // => 이러한 공통 정보들을 한 곳에 모아두고 공통으로 사용하지만, 각 페이지마다 필요한 정보를 더 추가해서 사용하고 싶다면 다음과 같이 사용하면 된다.
    //http://localhost:8080/template/layout
    @GetMapping("/layout")
    public String layout() {
        return "/template/layout/layoutMain";
    }

    //템플릿 레이아웃2
    // => 템플릿 레이아웃 확장 - 앞서 이야기한 개념을 <head> 정도에만 적용하는게 아니라 <html> 전체에 적용할 수도 있다
    //http://localhost:8080/template/layoutExtend
    @GetMapping("/layoutExtend")
    public String layoutExtend() {
        return "/template/layoutExtend/layoutExtendMain";
    }
}