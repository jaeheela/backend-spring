package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;
//MyBatis 적용1 - 기본
// => 이제부터 본격적으로 MyBatis를 사용해서 데이터베이스에 데이터를 저장해보자.
// => XML에 작성한다는 점을 제외하고는 JDBC 반복을 줄여준다는 점에서 기존 JdbcTemplate과 거의 유사하다.
// => 마이바티스 매핑 XML을 호출해주는 매퍼 인터페이스이다.
// => 이 인터페이스에는 @Mapper 애노테이션을 붙여주어야 한다. 그래야 MyBatis에서 인식할 수 있다.
// => 이 인터페이스의 메서드를 호출하면 다음에 보이는 xml 의 해당 SQL을 실행하고 결과를 돌려준다.
// => ItemMapper 인터페이스의 구현체에 대한 부분은 뒤에 별도로 설명한다.
// => 이제 같은 위치에 실행할 SQL이 있는 XML 매핑 파일을 만들어주면 된다.
// => 참고로 자바 코드가 아니기 때문에 src/main/resources 하위에 만들되, 패키지 위치는 맞추어 주어야한다.
// => [src/main/resources/hello/itemservice/repository/mybatis/ItemMapper.xml] 작성
@Mapper
public interface ItemMapper {

    void save(Item item);

    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearch);
}
