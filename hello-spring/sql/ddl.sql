-- H2 데이터베이스
-- => 개발이나 테스트 용도로 가볍고 편리한 DB, 웹 화면 제공

-- 1. H2 데이터베이스 다운로드 및 설치 : https://www.h2database.com
-- => 주의) h2 데이터베이스는 꼭 다음 링크에 들어가서 1.4.200 버전을 설치해주세요. 최근에 나온 2.0.206 버전을 설치하면 일부 기능이 정상 동작하지 않습니다. https://www.h2database.com/html/download-archive.html >
-- => 만약 이미 설치하고 실행까지 했다면 다시 설치한 이후에 ~/test.mv.db 파일을 꼭 삭제해주세요. 그렇지 않으면 다음 오류가 발생하면서 접속되지 않습니다.
-- => General error: "The write format 1 is smaller than the supported format 2 [2.0.206/5]" [50000-202] HY000/50000
-- => h2 데이터베이스 버전은 스프링 부트 버전에 맞춘다.

-- 2. H2 데이터베이스 실행
-- => cd bin >> chmod 755 h2.sh (권한 주기 - 윈도우 사용자는 안함) >> ./h2.sh (실행) >> 화면 확인
-- => 데이터베이스 파일 생성법 : [JDBC URL] jdbc:h2:~/test (최초 한번만 접속) >> ~/test.mv.db (해당 디렉토라에 파일이 생성되엇는지 확인) >>
-- => 이후부터는 [JDBC URL] jdbc:h2:tcp://localhost/~/test 로 접속
-- => 이렇게 해야 파일에 직접 접근하는 것이 아닌 소켓을 통해 접근할 수 있음 - 충돌나지 않음
-- => ctrl + c (실행종료)
-- => [드라이버 클래스] : org.h2.Driver
-- => [JDBC URL] : jdbc:h2:tcp://localhost/~/test
-- => [사용자명] : sa
-- => [비밀번호] :

-- 3. H2 데이터베이스에 접근해 member 테이블 생성
-- drop table if exists member CASCADE;
-- create table member
-- (
--     id   bigint generated by default as identity,
--     name varchar(255),
--     primary key (id)
-- );
-- H2 데이터베이스가 정상 생성되지 않을 때 다음과 같은 오류 메시지가 나오며 H2 데이터베이스가 정상 생성되지 않는 경우가 있다. (pdf참고)
-- 해결법) H2 데이터베이스를 종료 >> 다시시작 >> 웹 브라우저가 자동 실행되면 주소창에 다음과 같이 되어있다.(100.1.2.3이 아니라 임의의 숫자가 나온다.) >> 다음과 같이 앞 부분만 100.1.2.3를 localhost로 변경하고 Enter 입력 >> 나머지 부분은 절대 변경하면 안된다. (특히 뒤에 세션 부분이 변경되면 안된다.) >> 데이터베이스파일을 생성하면(jdbc:h2:~/test), 데이터베이스가 정상 생성된다.

-- 4. 테이블 관리를 위해 프로젝트 루트에 sql/ddl.sql 파일 생성

-- 5. 순수 Jdbc 환경 설정 - build.gradle 파일에 jdbc, h2 데이터베이스 관련 라이브러리 추가
-- implementation 'org.springframework.boot:spring-boot-starter-jdbc'
-- runtimeOnly 'com.h2database:h2'

-- 6. 스프링 부트 데이터베이스 연결 설정 추가
-- resources/application.properties 애서..
-- spring.datasource.url=jdbc:h2:tcp://localhost/~/test -- [JDBC URL]
-- spring.datasource.driver-class-name=org.h2.Driver -- [드라이버 클래스]
-- spring.datasource.username=sa -- [사용자명]

-- spring.jpa.show-sql=true
-- spring.jpa.hibernate.ddl-auto=none
-- ##spring.jpa.hibernate.ddl-auto=create
-- [show-sql] : JPA가 생성하는 SQL을 출력한다.
-- ddl-auto : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 none 를 사용하면 해당 기능을 끈다.
-- create 를 사용하면 엔티티 정보를 바탕으로 테이블도 직접 생성해준다. 해보자.
-- 주의) 스프링부트 2.4부터는 spring.datasource.username=sa 를 꼭 추가해주어야 한다. 그렇지 않으면 Wrong user name or password 오류가 발생한다.
-- => 참고로 다음과 같이 마지막에 공백이 들어가면 같은 오류가 발생한다. spring.datasource.username=sa 공백 주의, 공백은 모두 제거해야 한다.
-- => 인텔리J 커뮤니티(무료) 버전의 경우 application.properties 파일의 왼쪽이 다음 그림고 같이 회색으로 나온다. 엔터프라이즈(유료) 버전에서 제공하는 스프링의 소스 코드를 연결해주는 편의 기능이 빠진 것인데, 실제 동작하는데는 아무런 문제가 없다.

--MEMBER 테이블 DDL
drop table if exists member CASCADE;
create table member
(
    id   bigint generated by default as identity, -- 값이 없으면 DB가 자동으로 ID값 채워줌
    name varchar(255),
    primary key (id)
);
