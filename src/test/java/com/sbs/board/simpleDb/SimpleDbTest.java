package com.sbs.board.simpleDb;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.global.simpleDb.SimpleDb;
import com.sbs.board.global.simpleDb.Sql;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class SimpleDbTest {
  private SimpleDb simpleDb;

  @BeforeAll // 모든 테스트 메서드 실행 전에 한 번만 실행
  public void beforeAll() {
    simpleDb = new SimpleDb("localhost", "sbsst", "sbs123414", "JDBC_board");
    simpleDb.setDevMode(true); // 개발 모드 활성화 (디버깅을 위해 SQL 쿼리 출력)

    createArticleTable();
  }

  @BeforeEach // 각 테스트 메서드 실행 전에 실행
  public void beforeEach() {
    simpleDb.run("TRUNCATE article"); // article 테이블 초기화

    makeArticleTestData(); // 테스트 데이터 생성

    createMemberTable();
  }

  private void makeArticleTestData() {
    int memberId = 1;

    IntStream.rangeClosed(1, 5).forEach(i -> {
      String title = "제목 %d".formatted(i);
      String content = "내용 %d".formatted(i);

      Sql sql = simpleDb.genSql();
      sql.append("INSERT INTO article");
      sql.append("SET regDate = NOW()");
      sql.append(", updateDate = NOW()");
      sql.append(", title = ?", title);
      sql.append(", content = ?", content);
      sql.append(", memberId = ?", memberId);

      sql.insert();
    });
  }

  private void createArticleTable() {
    simpleDb.run("DROP TABLE IF EXISTS article");

    simpleDb.run("""
        CREATE TABLE article (
        	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
        	regDate DATETIME NOT NULL,
        	updateDate DATETIME NOT NULL,
        	title CHAR(100) NOT NULL,
        	content TEXT NOT NULL,
        	memberId INT UNSIGNED NOT NULL
        )
        """);
  }

  private void createMemberTable() {
    simpleDb.run("DROP TABLE IF EXISTS member");

    simpleDb.run("""
        CREATE TABLE `member` (
        	id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
        	regDate DATETIME NOT NULL,
        	updateDate DATETIME NOT NULL,
        	username CHAR(50) UNIQUE NOT NULL,
        	password CHAR(150) NOT NULL,
        	name CHAR(50) NOT NULL
        )
        """);
  }

  @Test
  @DisplayName("INSERT 테스트")
  public void t1() {
    int memberId = 1;

    int no = 6;
    String title = "제목 %d".formatted(no);
    String content = "내용 %d".formatted(no);

    Sql sql = simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", title = ?", title);
    sql.append(", content = ?", content);
    sql.append(", memberId = ?", memberId);

    long newId = sql.insert(); // Auto Increment ID 반환

    // 들어온 번호가 0보다 큰지 확인
    assertThat(newId).isGreaterThan(0);
  }

  @Test
  @DisplayName("SELECT 테스트 1 : 정순 조회")
  public void t2() {

    Sql sql = simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id ASC");

    List<Map<String, Object>> articleRows = sql.selectRows();

    // 정순 체크
    IntStream.range(0, articleRows.size()).forEach(i -> {
      long id = i + 1;

      Map<String, Object> articleRow = articleRows.get(i);

      // isInstanceOf : LocalDateTime 타입인지 확인
      assertThat(articleRow.get("id")).isEqualTo(id);
      assertThat(articleRow.get("regDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("regDate")).isNotNull();
      assertThat(articleRow.get("updateDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("updateDate")).isNotNull();
      assertThat(articleRow.get("title")).isEqualTo("제목 %d".formatted(id));
      assertThat(articleRow.get("content")).isEqualTo("내용 %d".formatted(id));
    });

    assertThat(articleRows.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("SELECT 테스트 2 : 역순 조회")
  public void t3() {

    Sql sql = simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id DESC");

    List<Map<String, Object>> articleRows = sql.selectRows();

    // 정순 체크
    IntStream.range(0, articleRows.size()).forEach(i -> {
      long id = articleRows.size() - i;

      Map<String, Object> articleRow = articleRows.get(i);

      // isInstanceOf : LocalDateTime 타입인지 확인
      assertThat(articleRow.get("id")).isEqualTo(id);
      assertThat(articleRow.get("regDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("regDate")).isNotNull();
      assertThat(articleRow.get("updateDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("updateDate")).isNotNull();
      assertThat(articleRow.get("title")).isEqualTo("제목 %d".formatted(id));
      assertThat(articleRow.get("content")).isEqualTo("내용 %d".formatted(id));
    });

    assertThat(articleRows.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("SELECT 테스트 3 : 원하는 게시물 번호 조회")
  public void t4() {
    Sql sql = simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = ?", 1);

    Map<String, Object> articleRow = sql.selectRow();

    assertThat(articleRow.get("id")).isEqualTo(1L);
    assertThat(articleRow.get("regDate")).isInstanceOf(LocalDateTime.class);
    assertThat(articleRow.get("regDate")).isNotNull();
    assertThat(articleRow.get("updateDate")).isInstanceOf(LocalDateTime.class);
    assertThat(articleRow.get("updateDate")).isNotNull();
    assertThat(articleRow.get("title")).isEqualTo("제목 1");
    assertThat(articleRow.get("content")).isEqualTo("내용 1");
  }

  @Test
  @DisplayName("SELECT 테스트 4 : 원하는 게시물 유무 확인")
  public void t5() {
    Sql sql = simpleDb.genSql();

    /*
    SELECT COUNT(*) > 0
    FROM article
    WHERE id = 1;
    */

    sql.append("SELECT COUNT(*) > 0")
        .append("FROM article")
        .append("WHERE id = ?", 6);

    boolean isExists = sql.selectBoolean();
    assertThat(isExists).isEqualTo(false);
  }

  @Test
  @DisplayName("UPDATE 테스트")
  public void t6() {
    Sql sql = simpleDb.genSql();

    /*
    UPDATE article
    SET title = 'new 제목'
    WHERE id IN (1, 2, 3, 4);
    */

    sql.append("UPDATE article")
        .append("SET title = ?", "new 제목")
        .append("WHERE id IN (?, ?, ?, ?)", 1, 2, 3, 4);

    long affectedRowsCount = sql.update();
    assertThat(affectedRowsCount).isEqualTo(4);
  }

  @Test
  @DisplayName("DELETE 테스트")
  public void t7() {
    // DELETE FROM article WHERE id = 1;
    Sql sql = simpleDb.genSql();
    sql.append("DELETE FROM article")
        .append("WHERE id = ?", 1);

    long affectedRowsCount = sql.delete();
    assertThat(affectedRowsCount).isEqualTo(1);
  }

  @Test
  @DisplayName("selectRows, Article")
  public void t8() {
    Sql sql = simpleDb.genSql();
    sql.append("SELECT *")
        .append("FROM article")
        .append("ORDER BY id ASC");

    // DB에서 데이터 2차원으로 날라옴
    // selectRows로 가져온 데이터를 Article 객체로 변환
    List<Article> articles = sql.selectRows(Article.class);

    // 정순 체크
    IntStream.range(0, articles.size()).forEach(i -> {
      long id = i + 1;

      Article article = articles.get(i);

      // isInstanceOf : LocalDateTime 타입인지 확인
      assertThat(article.getId()).isEqualTo(id);
      assertThat(article.getRegDate()).isInstanceOf(LocalDateTime.class);
      assertThat(article.getRegDate()).isNotNull();
      assertThat(article.getUpdateDate()).isInstanceOf(LocalDateTime.class);
      assertThat(article.getUpdateDate()).isNotNull();
      assertThat(article.getTitle()).isEqualTo("제목 %d".formatted(id));
      assertThat(article.getContent()).isEqualTo("내용 %d".formatted(id));
    });
  }

  @Test
  @DisplayName("selectRow, Article")
  public void t9() {
    Sql sql = simpleDb.genSql();
    sql.append("SELECT *")
        .append("FROM article")
        .append("WHERE id = ?", 1);

    // DB에서 데이터 2차원으로 날라옴
    // selectRow로 가져온 데이터를 Article 객체로 변환
    Article article = sql.selectRow(Article.class);

    assertThat(article.getId()).isEqualTo(1L);
    // isInstanceOf : LocalDateTime 타입인지 확인
    // isNotNull : null이 아닌지 확인
    assertThat(article.getRegDate()).isInstanceOf(LocalDateTime.class);
    assertThat(article.getRegDate()).isNotNull();
    assertThat(article.getUpdateDate()).isInstanceOf(LocalDateTime.class);
    assertThat(article.getUpdateDate()).isNotNull();
    assertThat(article.getTitle()).isEqualTo("제목 1");
    assertThat(article.getContent()).isEqualTo("내용 1");
  }

  @Test
  @DisplayName("회원 1명 추가")
  public void t10() {
    String username = "user1";
    String password = "1234";
    String name = "홍길동";

    Sql sql = simpleDb.genSql();
    sql.append("INSERT INTO `member`");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", username = ?", username);
    sql.append(", password = ?", password);
    sql.append(", name = ?", name);

    long newId = sql.insert();

    assertThat(newId).isEqualTo(1L);
  }
}
