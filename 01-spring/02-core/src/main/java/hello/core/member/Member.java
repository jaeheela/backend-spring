package hello.core.member;

//회원 도메인 개발 - 회원 엔티티
public class Member {
    private Long id;
    private String name;
    private Grade grade;

    //단축키 : ctrl + n
    public Member(Long id, String name, Grade grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    //단축키 : ctrl + n
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
