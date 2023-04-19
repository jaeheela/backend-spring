package hello.itemservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//홈으로 요청이 왔을 때 items로 이동하는 HomeController
@Controller
@RequiredArgsConstructor
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/items";
    }
}
