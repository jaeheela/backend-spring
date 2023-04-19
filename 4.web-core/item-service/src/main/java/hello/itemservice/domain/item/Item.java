package hello.itemservice.domain.item;

import lombok.Data;
//요구사항 분석 - 상품을 관리할 수 있는 서비스
// => 상품 도메인 모델 : 상품 ID, 상품명 가격, 수량
// => 상품 관리 기능 : 상품 목록, 상품 상세, 상품 등록, 상품 수정

//상품 도메인 개발
//Item - 상품 객체 >> ItemRepository - 상품 저장소 >> ItemRepositoryTest - 상품 저장소 테스트

//서비스 제공 흐름
// => 요구사항이 정리되고 디자이너, 웹 퍼블리셔, 백엔드 개발자가 업무를 나누어 진행한다.
// => 디자이너: 요구사항에 맞도록 디자인하고, 디자인 결과물을 웹 퍼블리셔에게 넘겨준다.
// => 웹 퍼블리셔: 다자이너에서 받은 디자인을 기반으로 HTML, CSS를 만들어 개발자에게 제공한다.
// => 백엔드 개발자: 디자이너, 웹 퍼블리셔를 통해서 HTML 화면이 나오기 전까지 시스템을 설계하고, 핵심 비즈니스 모델을 개발한다.
// => 이후 HTML이 나오면 이 HTML을 뷰 템플릿으로 변환해서 동적으로 화면을 그리고, 또 웹 화면의 흐름을 제어한다.
// => 참고로 React, Vue.js 같은 웹 클라이언트 기술을 사용하고, 웹 프론트엔드 개발자가 별도로 있으면, 웹 프론트엔드 개발자가 웹 퍼블리셔 역할까지 포함해서 하는 경우도 있다.
// => 웹 클라이언트 기술을 사용하면, 웹 프론트엔드 개발자가 HTML을 동적으로 만드는 역할과 웹 화면의 흐름을 담당한다.
// => 이 경우 백엔드 개발자는 HTML 뷰 템플릿을 직접 만지는 대신에, HTTP API를 통해 웹 클라이언트가 필요로 하는 데이터와 기능을 제공하면 된다.

//상품 서비스 HTML
// => 핵심 비즈니스 로직을 개발하는 동안, 웹 퍼블리셔는 HTML 마크업을 완료했다. 다음 파일들을 경로에 넣고 잘 동작하는지 확인해보자.
// => 참고로 HTML을 편리하게 개발하기 위해 부트스트랩 사용했다. 먼저 필요한 부트스트랩 파일을 설치하자
// => 부트스트랩 공식 사이트: https://getbootstrap.com 부트스트랩을 다운 >> 이동: https://getbootstrap.com/docs/5.0/getting-started/download/ Compiled CSS and JS 항목 다운 >> bootstrap.min.css를 복사해 다음 폴더에 추가 - [resources/static/css/bootstrap.min.css]
// => 참고로 부트스트랩(Bootstrap)은 웹사이트를 쉽게 만들 수 있게 도와주는 HTML, CSS, JS 프레임워크이다. 하나의 CSS로 휴대폰, 태블릿, 데스크탑까지 다양한 기기에서 작동한다. 다양한 기능을 제공하여 사용자가 쉽게 웹사이트를 제작, 유지, 보수할 수 있도록 도와준다.
// => HTML, css 파일 : [resources/static/css/bootstrap.min.css 부트스트랩 다운로드]
// => 참고로 /resources/static 에 넣어두었기 때문에 스프링 부트가 정적 리소스를 제공한다. http://localhost:8080/html/items.html
// => 그런데 정적 리소스여서 해당 파일을 탐색기를 통해 직접 열어도 동작하는 것을 확인할 수 있다.
// => 참고로 이렇게 정적 리소스가 공개되는 /resources/static 폴더에 HTML을 넣어두면, 실제 서비스에서도 공개된다. 서비스를 운영한다면 지금처럼 공개할 필요없는 HTML을 두는 것은 주의하자.
// => [상품 목록 HTML] : resources/static/html/items.html
// => [상품 상세 HTML] : resources/static/html/item.html
// => [상품 등록 폼 HTML] : resources/static/html/addForm.html
// => [상품 수정 폼 HTML] : resources/static/html/editForm.html
@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
