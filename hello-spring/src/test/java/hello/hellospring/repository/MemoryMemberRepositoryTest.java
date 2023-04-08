package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    //테스트 메소드 하나 끝날때마다 반복적으로 호출될 메소드 - 콜백메소드
    @AfterEach
    public void afterEach(){
        repository.clearStore(); //테스트 메소드가 끝날때마다 레파지토리를 지워줌 - 테스트메소드의 순서 상관없어짐
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result=repository.findById(member.getId()).get();
        //System.out.println("result = "+(result==member)); //result = true

        //가져온 결과값[result]이 [member]와 같나요??
        //Assertions.assertEquals(member,result); //같으면 초록불 들어옴
        //Assertions.assertEquals(member,null); //다르면 빨간불 들어옴
        //Expected :hello.hellospring.domain.Member@77f1baf5 - 기대한건 이건데
        //Actual   :null - 실제값은 null이 들어왔어!

        //Assertions.assertThat(member).isEqualTo(null);
        Assertions.assertThat(member).isEqualTo(result);
    }

    @Test
    public void findNyName(){
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result=repository.findByName("spring1").get();

        Assertions.assertThat(result).isEqualTo(member1);
        //Assertions.assertThat(result).isEqualTo(member2);

    }

    @Test
    public void findAll(){
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result= repository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(2);
        //Assertions.assertThat(result.size()).isEqualTo(3);
    }

}
