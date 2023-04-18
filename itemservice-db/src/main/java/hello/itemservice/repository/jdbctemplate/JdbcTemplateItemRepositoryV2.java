package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//JdbcTemplate - 이름 지정 파라미터 2
// => 이름 지정 바인딩에서 자주 사용하는 파라미터의 종류는 크게 3가지가 있다.
//1.Map
// => 단순히 Map 을 사용한다.
// => ex) findById()
// => Map<String, Object> param = Map.of("id", id);
// => Item item = template.queryForObject(sql, param, itemRowMapper());
//2.MapSqlParameterSource(SqlParameterSource)
// => Map과 유사한데, SQL 타입을 지정할 수 있는 등 SQL에 좀 더 특화된 기능을 제공한다.
// => SqlParameterSource 인터페이스의 구현체이다. MapSqlParameterSource는 메서드 체인을 통해 편리한 사용법도 제공한다.
// => ex) update()
// => SqlParameterSource param = new MapSqlParameterSource()
// =>    .addValue("itemName", updateParam.getItemName()).addValue("price", updateParam.getPrice()).addValue("quantity", updateParam.getQuantity())
// =>    .addValue("id", itemId); //이 부분이 별도로 필요하다.
// => template.update(sql, param);
//3.BeanPropertySqlParameterSource(SqlParameterSource)
// => 자바빈 프로퍼티 규약을 통해서 자동으로 파라미터 객체를 생성한다. 예) ( getXxx() -> xxx, getItemName() -> itemName )
// => 예를 들어서 getItemName() , getPrice() 가 있으면 [key=itemName, value=상품명값] [key=price, value=가격값]과 같은 데이터를 자동으로 만들어낸다.
// => SqlParameterSource 인터페이스의 구현체이다.
// => ex) save(), findAll()
// => SqlParameterSource param = new BeanPropertySqlParameterSource(item);
// => KeyHolder keyHolder = new GeneratedKeyHolder();
// => template.update(sql, param, keyHolder);
// => 여기서 보면 BeanPropertySqlParameterSource 가 많은 것을 자동화 해주기 때문에 가장 좋아보이지만,
// => BeanPropertySqlParameterSource 를 항상 사용할 수 있는 것은 아니다.
// => 예를 들어서 update() 에서는 SQL에 :id 를 바인딩 해야 하는데, update() 에서 사용하는 ItemUpdateDto 에는 itemId 가 없다.
// => 따라서 BeanPropertySqlParameterSource 를 사용할 수 없고, 대신에 MapSqlParameterSource 를 사용했다.

//BeanPropertyRowMapper
// => 이번 코드에서 V1 과 비교해서 변화된 부분이 하나 더 있다. 바로 BeanPropertyRowMapper 를 사용한 것이다.
// JdbcTemplateItemRepositoryV1 - itemRowMapper()
//   private RowMapper<Item> itemRowMapper() {
//      return (rs, rowNum) -> {
//          Item item = new Item();
//          item.setId(rs.getLong("id"));
//          item.setItemName(rs.getString("item_name"));
//          item.setPrice(rs.getInt("price"));
//          item.setQuantity(rs.getInt("quantity"));
//          return item;
//   }; }
//   JdbcTemplateItemRepositoryV2 - itemRowMapper()
//      private RowMapper<Item> itemRowMapper() {
//         return BeanPropertyRowMapper.newInstance(Item.class); //camel 변환 지원
//   }
// => BeanPropertyRowMapper 는 ResultSet 의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환한다.
// => 예를 들어서 데이터베이스에서 조회한 결과가 select id, price 라고 하면 다음과 같은 코드를 작성해준다. (실제로는 리플렉션 같은 기능을 사용한다.)
// => Item item = new Item(); item.setId(rs.getLong("id")); item.setPrice(rs.getInt("price"));
// => 데이터베이스에서 조회한 결과 이름을 기반으로 setId() , setPrice() 처럼 자바빈 프로퍼티 규약에 맞춘 메서드를 호출하는 것이다.

//별칭 as
// => 그런데 select item_name 의 경우 setItem_name() 이라는 메서드가 없기 때문에 골치가 아프다.
// => 이런 경우 개발자가 조회 SQL을 select item_name as itemName로  고치면 된다.
// => 별칭 as를 사용해서 SQL 조회 결과의 이름을 변경하는 것 이다. 실제로 이 방법은 자주 사용된다.특히 데이터베이스 컬럼 이름과 객체 이름이 완전히 다를 때 문제를 해결할 수 있다.
// =>예를 들어서 데이터베이스에는 member_name 이라고 되어 있는데 객체에 username 이라고 되어 있다면 다음과 같이 해결할 수 있다.
// => select member_name as username 이렇게 데이터베이스 컬럼 이름과 객체의 이름이 다를 때 별칭( as )을 사용해서 문제를 많이 해결한다.
// => JdbcTemplate 은 물론이고, MyBatis 같은 기술에서도 자주 사용된다.

//관례의 불일치
// => 자바 객체는 카멜( camelCase ) 표기법을 사용한다. itemName 처럼 중간에 낙타 봉이 올라와 있는 표기법이다.
// => 반면에 관계형 데이터베이스에서는 주로 언더스코어를 사용하는 snake_case 표기법을 사용한다. item_name 처럼 중간에 언더스코어를 사용하는 표기법이다.
// => 이 부분을 관례로 많이 사용하다 보니 BeanPropertyRowMapper 는 언더스코어 표기법을 카멜로 자동 변환해준다.
// => 따라서 select item_name 으로 조회해도 setItemName() 에 문제 없이 값이 들어간다.
// => 정리하면 snake_case 는 자동으로 해결되니 그냥 두면 되고, 컬럼 이름과 객체 이름이 완전히 다른 경우에는 조회 SQL에서 별칭을 사용하면 된다.
/**
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * Map
 *
 * BeanPropertyRowMapper
 *
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    //기본
    // => JdbcTemplateItemRepositoryV2 는 ItemRepository 인터페이스를 구현했다.
    // => this.template = new NamedParameterJdbcTemplate(dataSource) : NamedParameterJdbcTemplate 도 내부에 dataSource 가 필요하다.
    // => JdbcTemplateItemRepositoryV2 생성자를 보면 의존관계 주입은 dataSource 를 받고 내부에서 NamedParameterJdbcTemplate 을 생성해서 가지고 있다.
    // => 스프링에서는 JdbcTemplate 관련 기능을 사용할 때 관례상 이 방법을 많이 사용한다. 물론 NamedParameterJdbcTemplate 을 스프링 빈으로 직접 등록하고 주입받아도 된다.
    // => 추가로 NamedParameterJdbcTemplate 은 데이터베이스가 생성해주는 키를 매우 쉽게 조회하는 기능도 제공해준다.
    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    //save() : SQL에서 다음과 같이 ? 대신에 :파라미터이름 을 받는 것을 확인할 수 있다.
    // => insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)"
    // => 파라미터를 전달하려면 Map 처럼 key , value 데이터 구조를 만들어서 전달해야 한다.
    // => 여기서 key 는 :파리이터이름 으로 지정한, 파라미터의 이름이고 , value는 해당 파라미터의 값이 된다.
    // => 다음 코드를 보면 이렇게 만든 파라미터( param )를 전달하는 것을 확인할 수 있다. template.update(sql, param, keyHolder);
    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (:itemName, :price, :quantity)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);
        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId); //이 부분이 별도로 필요하다.

        template.update(sql, param);
    }


    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = :id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity from item";
        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper());
    }

    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); //camel 변환 지원
    }
}
