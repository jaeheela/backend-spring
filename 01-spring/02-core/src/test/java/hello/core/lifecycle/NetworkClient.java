package hello.core.lifecycle;

//빈 생명주기 콜백 시작
// => 데이터베이스 커넥션 풀이나, 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리 해두고,
// => 애플리케이션 종료 시점에 연결을 모두 종료하는 작업을 진행하려면, 객체의 초기화와 종료 작업이 필요하다.
// => 이번시간에는 스프링을 통해 이러한 초기화 작업과 종료 작업을 어떻게 진행하는지 예제로 알아보자.
// => 간단하게 외부 네트워크에 미리 연결하는 객체를 하나 생성한다고 가정해보자.
// => 실제로 네트워크에 연결하는 것은 아니고, 단순히 문자만 출력하도록 했다.
// => 이 NetworkClient 는 애플리케이션 시작 시점에 connect() 를 호출해서 연결을 맺어두어야 하고,
// => 애플리케이션이 종료되면 disConnect() 를 호출해서 연결을 끊어야 한다.

//예제 코드, 테스트 하위에 생성
public class NetworkClient {
    private String url;
    public NetworkClient() { System.out.println("생성자 호출, url = " + url);
        connect();
        call("초기화 연결 메시지");
    }
    public void setUrl(String url) {
        this.url = url;
    }
    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }
    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }
    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }
}
