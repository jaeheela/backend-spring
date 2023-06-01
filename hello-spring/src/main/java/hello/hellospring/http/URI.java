package hello.hellospring.http;

public class URI {

    //[URI와 웹 브라우저 요청 흐름]

    //1. URI(Uniform Resource Identifier)
    // => Uniform : 리소스를 식별하는 통일된 방식
    // => Resource : 자원,URI로 식별할 수 있는 모든 것(제한없음)
    // => Identifier : 다른 항목과 구분하는데 필요한 정보
    // => 로케이터(locator), 이름(name)또는 둘 다 추가로 분류될 수 있음
    // => URL(Locator) : 리소스의 위치 - 리소스가 이 위치에 있어요!! - 우리가 흔히 웹브라우저에서 사용하는 주소
    // => URN(Name) : 리소스의 이름 - 이름만 가지고는 주소를 찾아갈 수 없기에 실제로 사용하기는 힘듦
    // => 위치는 변할 수 있지만, 이름은 변하지 않으므로 URN 이름만으로 리소스를 찾을 수 있는 방법이 보편화되지 않음
    // => 앞으로 URI = URL을 같은 취급 하겠음
    //URL 분석
    // => scheme://[userinfo@]host[:port][/path][?query][#fragment]
    // => https://google.com/search?q=hello&hl=ko
    // => [scheme] = [https] : 주로 프로토콜(어떤 방식으로 자원에 접근할 것인지 정해놓은 규칙)사용  ex) http(80포트 - 생략가능), https(http에 보안사용을 추가한 것 - HTTP Secure , 443포트 - 생략가능), ftp ...
    // => [Userinfo@] = [없음] : URL에 사용자정보를 포함해서 인증할 때 사용한다. 하지만 거의 사용하지 않는다.
    // => [host] : [google.com] : 호스트명, 도메인명 또는 IP주소를 직접 입력한다.
    // => [port] = [443] : 접속 포트 , 일반적으로 생략가능하며, 생략시 http는 80, https는 443이다.
    // => [/path] = [/search] : 리소스 경로, 계층적 구조로 되어있다. ex) /home/file1.jpg , /members , /members/100, /item/iphone12
    // => [?query] = [q=hello&hl=ko] : key=value형태,  웹서버에 제공하는 파라미터, 문자형태, ?로 시작하며 &로 추가가 가능  ex) ?keyA=valueA&keyB=valueB, query parameter, query string등으로 불림,
    // => [fragment] = [없음] : html 내부 북마크 등에 사용한다. 서버에 전송하는 정보가 아님

    //2. 웹 브라우저 요청 흐름
    //https://google.com/search?q=hello&hl=ko 요청 시 일어나는 일
    // => 1. DNS 조회 : google.com을 DNS를 조회해서 IP주소(IP: 200.200.200.2) 찾음
    // => 2. HTTPS PORT 생략(443)
    // => 3. 웹 브라우저가 HTTP 요청 메세지 생성 : GET /search?q=hello&hl=ko HTTP/1.1
    // =>                                  Host: www.google.com
    // =>
    // => 4. SOCKET 라이브러리를 통해 TCP/IP계층에 전달
    // => 5. 이전단계에서 찾은 IP와 PORT정보를 가지고 SYN, SYN+ACK, ACK 과정을 통해 서버와 연결(목적IP : 목적PORT)
    // => 6. 연결이 성공되면 TCP/IP 계층으로 데이터 전달
    // => 7. HTTP 메세지를 포함한 TCP/IP 패킷 생성해 전달 - 요청패킷
    // => 8. 구글서버에게 요청 패킷 도착
    // => 9. 구글서버는 TCP/IP 패킷은 버리고 HTTP 메세지만 해석함 >> 내부 HTTP 메서드를 해석헤 정보에 맞는 동작을 함
    // => 10. 구글서버는 응답 HTTP 메세지 생성 : HTTP/1.1 200 OK
    // =>                                Content-Type: text/html;charset=UTF-8
    // =>                                Content-Length: 3423
    // =>
    // =>                                <html>
    // =>                                  <body>...</body>
    // =>                                </html>
    // => 11. 구글서버가 응답 HTTP 메세지를 TCP/IP 패킷에 감싸서 웹브라우저에게 패킷 전달 - 응답패킷
    // => 12. 클라이언트에서는 응답메세지를 받아 맞는 동작을 함 (ex: 렌더링)
}
