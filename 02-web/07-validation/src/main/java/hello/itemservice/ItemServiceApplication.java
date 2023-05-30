package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
}

/*
//글로벌 Validator 설정하는 법 - 모든 컨트롤러에 다 적용
// => 이렇게 글로벌 설정을 추가할 수 있다.
// => 기존 컨트롤러의 @InitBinder 를 제거해도 글로벌 설정으로 정상 동작하는 것을 확인할 수 있다.
// => 이어지는 다음 강의를 위해서 글로벌 설정은 꼭 제거해두자.
// => 주의) 글로벌 설정을 하면 다음에 설명할 BeanValidator가 자동 등록되지 않는다.
// => 글로벌 설정 부분은 주석처리 해두자. 참고로 글로벌 설정을 직접 사용하는 경우는 드물다.
@SpringBootApplication
public class ItemServiceApplication implements WebMvcConfigurer {
	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
	@Override
	public Validator getValidator() {
		return new ItemValidator();
	}
}
*/



