package com.sbs.board.simpleDb;

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
  }

  private void makeArticleTestData() {
    IntStream.rangeClosed(1, 5).forEach(i -> {
      String title = "제목 %d".formatted(i);
      String content = "내용 %d".formatted(i);

      Sql sql = simpleDb.genSql();
      sql.append("INSERT INTO article");
      sql.append("SET regDate = NOW()");
      sql.append(", updateDate = NOW()");
      sql.append(", title = ?", title);
      sql.append(", content = ?", content);

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
        	content TEXT NOT NULL
        )
        """);
  }

  @Test
  @DisplayName("INSERT 테스트")
  public void t1() {
    int no = 6;
    String title = "제목 %d".formatted(no);
    String content = "내용 %d".formatted(no);

    /*
    simpleDb.run("""
         INSERT INTO article
          SET regDate = NOW(),
          updateDate = NOW(),
          title = ?,
          content = ?
        """, title, content);
     */

    Sql sql = simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", title = ?", title);
    sql.append(", content = ?", content);

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
}
