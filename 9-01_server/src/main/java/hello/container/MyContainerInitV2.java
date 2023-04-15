package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
//서블릿 컨테이너 초기화( ServletContainerInitializer )는 앞서 알아보았다.
//그런데 애플리케이션 초기화( AppInit )는 어떻게 실행되는 것일까? 다음 코드를 만들어 보자.

//애플리케이션 초기화 과정
//1. @HandlesTypes 애노테이션에 애플리케이션 초기화 인터페이스를 지정한다.
//여기서는 앞서 만든 AppInit.class 인터페이스를 지정했다.
//2. 서블릿 컨테이너 초기화( ServletContainerInitializer )는 파라미터로 넘어오는 Set<Class<?>> c 에 애플리케이션 초기화 인터페이스의 구현체들을 모두 찾아서 클래스 정보로 전달한다.
//여기서는 @HandlesTypes(AppInit.class) 를 지정했으므로 AppInit.class 의 구현체인 AppInitV1Servlet.class 정보가 전달된다.
//참고로 객체 인스턴스가 아니라 클래스 정보를 전달하기 때문에 실행하려면 객체를 생성해서 사용해야 한다.
//3. appInitClass.getDeclaredConstructor().newInstance() 리플렉션을 사용해서 객체를 생성한다.
//참고로 이 코드는 new AppInitV1Servlet() 과 같다 생각하면 된다.
//4. appInit.onStartup(ctx) 애플리케이션 초기화 코드를 직접 실행하면서 서블릿 컨테이너 정보가 담긴 ctx 도 함께 전달한다.

//MyContainerInitV2 등록
//MyContainerInitV2 를 실행하려면 서블릿 컨테이너에게 알려주어야 한다. 설정을 추가하자.
//=> [resources/META-INF/services/jakarta.servlet.ServletContainerInitializer] : hello.container.MyContainerInitV2 추 :

@HandlesTypes(AppInit.class)
public class MyContainerInitV2 implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("MyContainerInitV2.onStartup");
        System.out.println("MyContainerInitV2 c = " + c);
        System.out.println("MyContainerInitV2 ctx = " + ctx);

        //class hello.container.AppInitV1Servlet
        for (Class<?> appInitClass : c) {
            try {
                //new AppInitV1Servlet()과 같은 코드
                AppInit appInit = (AppInit) appInitClass.getDeclaredConstructor().newInstance();
                appInit.onStartup(ctx);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}

//WAS를 실행해보자. 실행 로그
//  MyContainerInitV1.onStartup
//  MyContainerInitV1 c = null
//  MyContainerInitV1 ctx =
//  org.apache.catalina.core.ApplicationContextFacade@38dd0980
//  MyContainerInitV2.onStartup
//  MyContainerInitV2 c = [class hello.container.AppInitV1Servlet]
//  MyContainerInitV2 container =
//  org.apache.catalina.core.ApplicationContextFacade@38dd0980
//  AppInitV1Servlet.onStartup

//서블릿을 실행해보자
//실행 : http://localhost:8080/hello-servlet
//결과 :hello servlet!

//정리
//초기화는 다음 순서로 진행된다
// 1. 서블릿 컨테이너 초기화 실행 - resources/META-INF/services/jakarta.servlet.ServletContainerInitializer
// 2. 애플리케이션 초기화 실행 - @HandlesTypes(AppInit.class)

//참고
//서블릿 컨테이너 초기화만 있어도 될 것 같은데, 왜 이렇게 복잡하게 애플리케이션 초기화라는 개념을 만들었을까?
//1. 편리함
//서블릿 컨테이너를 초기화 하려면 ServletContainerInitializer 인터페이스를 구현한 코드를 만들어야 한다.
//여기에 추가로 파일에 해당 코드를 직접 지정해주어야 한다. 애플리케이션 초기화는 특정 인터페이스만 구현하면 된다.
//2. 의존성
//애플리케이션 초기화는 서블릿 컨테이너에 상관없이 원하는 모양으로 인터페이스를 만들 수 있다.
//이를 통해 애플리케이션 초기화 코드가 서블릿 컨테이너에 대한 의존을 줄일 수 있다.
//특히 ServletContext ctx 가 필요없는 애플리케이션 초기화 코드라면 의존을 완전히 제거할 수도 있다.


//스프링 컨테이너 등록
//이번에는 WAS와 스프링을 통합해보자.
//앞서 배운 서블릿 컨테이너 초기화와 애플리케이션 초기화를 활용하면 된다. - hello.spring.HelloController.java