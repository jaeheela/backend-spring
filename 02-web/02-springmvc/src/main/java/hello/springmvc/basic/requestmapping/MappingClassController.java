package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;
//요청 매핑 - API 예시
// => 회원 관리를 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아보자. (실제 데이터가 넘어가는 부분은 생략하고 URL 매핑만)

//회원 관리 API
// => 회원 목록 조회: GET /users
// => 회원 등록: POST /users
// => 회원 조회: GET /users/{userId}
// => 회원수정: PATCH /users/{userId}
// => 회원 삭제: DELETE /users/{userId}

// => Postman으로 테스트
// => 매핑 방법을 이해했으니, 이제부터 HTTP 요청이 보내는 데이터들을 스프링 MVC로 어떻게 조회하는지 알아보자.

@RestController
@RequestMapping("/mapping/users") //클래스 레벨에 매핑 정보를 두면 메서드 레벨에서 해당 정보를 조합해서 사용함
public class MappingClassController {

    /**
     * GET /mapping/users
     */
    // => http://localhost:8080//mapping/users
    @GetMapping
    public String user() {
        return "get users";
    }

    /**
     * POST /mapping/users
     */
    // => http://localhost:8080//mapping/users
    @PostMapping
    public String addUser() {
        return "post user";
    }

    /**
     * GET /mapping/users/{userId}
     */
    // => http://localhost:8080//mapping/users/jaehee
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }

    /**
     * PATCH /mapping/users/{userId}
     */
    // => http://localhost:8080//mapping/users/jaehee
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }

    /**
     * DELETE /mapping/users/{userId}
     */
    // => http://localhost:8080//mapping/users/jaehee
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }

}
