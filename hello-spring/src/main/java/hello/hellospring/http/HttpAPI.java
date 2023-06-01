package hello.hellospring.http;

public class HttpAPI {

    //[HTTP API 설계 예시]
    // => 참고하면 좋은 URI 설계 개념 : https://restfulapi.net/resource-naming
    // => 문서(document) : 단일 개념(파일 하나, 객체 인스턴스, 데이터베이스 row) , 예) /members/100, /files/star.jpg (개발시 거의 대부분)
    // => 컬렉션(collection) : 서버가 관리하는 리소스 디렉터리, 서버가 리소스의 URI를 생성하고 관리 예) /members (개발시 거의 대부분)
    // => 스토어(store) : 클라이언트가 관리하는 자원 저장소 클라이언트가 리소스의 URI를 알고 관리 예) /files
    // => 컨트롤러(controller), 컨트롤 URI : 문서, 컬렉션, 스토어로 해결하기 어려운 추가 프로세스 실행 동사를 직접 사용 예) /members/{id}/delete

    //1. HTTP API - 컬렉션
    // => [POST] 기반 등록, 서버가 리소스 URI 결정
    // => 예) 회원 관리 API 제공

    //2. HTTP API - 스토어
    // => [PUT] 기반 등록, 클라이언트가 리소스 URI 결정
    // => 예) 정적 컨텐츠 관리, 원격 파일 관리

    //3. HTML FORM 사용
    // => 순수 HTML + HTML form 사용, [GET],[POST]만 지원
    // => 예) 웹 페이지 회원 관리


    //1. 회원 관리 시스템 API 설계 - POST 기반 등록
    //회원 목록 /members -> GET
    //회원 목록 /members?no=1 -> GET
    //회원 등록 /members -> POST (신규 자원 등록 POST의 특징)
    // => 클라이언트는 등록될 리소스의 URI를 모른다. ex) 회원 등록 [POST] /members => /members
    // => 서버가 새로 등록된 리소스 URI를 생성해준다. ex) HTTP/1.1 201 Created Location: /members/100
    // => 이를 컬렉션(Collection) 형식이라 한다. - 서버가 관리하는 리소스 디렉토리, 서버가 리소스의 URI를 생성하고 관리, 여기서 컬렉션은 /members
    // => 대부분 POST 기반의 컬렉션을 많이 사용한다.
    //회원 조회 /members/{id} -> GET
    //회원 수정 /members/{id} -> PATCH(가장좋음), PUT(overwrite), POST(둘다 애매)
    //회원 삭제 /members/{id} -> DELETE

    //2. 파일 관리 시스템 API 설계 - PUT 기반 등록
    //파일 목록 /files -> GET
    //파일 조회 /files/{filename} -> GET
    //파일 등록 /files/{filename} -> PUT (신규 자원 등록 PUT의 특징)
    // => 클라이언트가 리소스 URI를 알고 있어야 한다. ex) 파일 등록 [PUT] /files/{filename} =>  /files/star.jpg
    // => 클라이언트가 직접 리소스의 URI를 지정한다.
    // => 이를 스토어(Store) 형식이라 한다. - 클라이언트가 관리하는 리소스 저장소, 클라이언트가 리소스의 URI를 알고 관리, 여기서 스토어는 /files
    //파일 삭제 /files/{filename} -> DELETE
    //파일 대량 등록 /files -> POST

    //3. HTML FORM 사용
    // => HTML FORM은 GET, POST만 지원
    // => AJAX 같은 기술을 사용해서 해결 가능 -> 회원 API 참고
    // => 여기서는 순수 HTML, HTML FORM 이야기
    // => GET, POST만 지원하므로 제약이 있음
    //회원 목록 /members -> GET
    //회원 등록 폼 /members/new -> GET
    //회원 등록 /members/new, /members -> POST
    //회원 조회 /members/{id} -> GET
    //회원 수정 폼 /members/{id}/edit -> GET
    //회원 수정 /members/{id}/edit, /members/{id} -> POST
    //회원 삭제 /members/{id}/delete -> POST (컨트롤 URI)
    // => 컨트롤 URI : GET, POST만 지원하므로 제약이 있음, 이런 제약을 해결하기 위해 동사로 된 리소스 경로 사용
    // => POST의 /new, /edit, /delete가 컨트롤 URI , HTTP 메서드로 해결하기 애매한 경우 사용(HTTP API 포함)


}
