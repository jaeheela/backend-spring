package hello.hellospring.domain;

public class Member {

    private Long id;//시스템의 자동증가값 이용
    private String name; //고객의 입력값 이용(Member 매개변수로 넘어올 것임)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
