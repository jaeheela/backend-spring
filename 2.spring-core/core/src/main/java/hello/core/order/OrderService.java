package hello.core.order;

//주문과 할인 도메인 개발 - 주문 서비스 인터페이스
public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}