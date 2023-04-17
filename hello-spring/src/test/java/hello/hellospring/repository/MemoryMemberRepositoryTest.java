package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
//회원 리포지토리 메모리 구현체 테스트

//테스트 케이스 작성 팁
// => 작품을 만들기 전에 검증할 수 있는 틀을 먼저 만들 수도 있음 - 테스트주도개발
// => 테스트가 한개면 모르지만 수백개라면? 나중에 빌드하거나, gradlew 띄우고 테스트하면 테스트를 자동으로 돌려줌
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    //테스트 메소드 하나 끝날때마다 반복적으로 호출될 메소드 - 콜백메소드
    // => 테스트 메소드가 끝날때마다 레파지토리를 지워줌 - 테스트 메소드의 순서가 상관없어짐
    //@AfterEach 어노테이션
    // => 한번에 여러 테스트를 실행하면 메모리 DB에 직전 테스트의 결과가 남을 수 있다.
    // => 이렇게 되면 다음 이전 테스트 때문에 다음 테스트가 실패할 가능성이 있다.
    // => @AfterEach 를 사용하면 각 테스트가 종료될 때 마다 이 기능을 실행한다.
    // => 여기서는 메모리 DB에 저장된 데이터를 삭제한다.
    // => 테스트는 각각 독립적으로 실행되어야 한다. 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니다.
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        //given (상황)
        Member member = new Member();
        member.setName("spring");

        //when (행동)
        repository.save(member);

        //then (결과)
        Member result=repository.findById(member.getId()).get();
        //System.out.println("result = "+(result==member)); //result = true

        //가져온 결과값[result]이 [member]와 같나요??
        //Assertions.assertEquals(member,result); //같으면 초록불 들어옴
        //Assertions.assertEquals(member,null); //다르면 빨간불 들어옴
        //Expected :hello.hellospring.domain.Member@77f1baf5 - 기대한건 이건데
        //Actual   :null - 실제값은 null이 들어왔어!

        //Assertions.assertThat(result).isEqualTo(null);
        Assertions.assertThat(result).isEqualTo(member);
    }

    @Test
    public void findNyName(){
        //given (상황)
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when (행동)
        Member result=repository.findByName("spring1").get();

        //then (결과)
        Assertions.assertThat(result).isEqualTo(member1);
        //Assertions.assertThat(result).isEqualTo(member2);

    }

    @Test
    public void findAll(){
        //given (상황)
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);
        //when (행동)
        List<Member> result= repository.findAll();

        //then (결과)
        Assertions.assertThat(result.size()).isEqualTo(2);
        //Assertions.assertThat(result.size()).isEqualTo(3);
    }

}
