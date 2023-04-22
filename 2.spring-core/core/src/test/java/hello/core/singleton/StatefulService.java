package hello.core.singleton;

//상태를 유지할 경우 발생하는 문제점 예시
public class StatefulService {
    private int price; //상태를 유지하는 필드
    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; //여기가 문제!
    }
    public int getPrice() {
        return price;
    }
}
