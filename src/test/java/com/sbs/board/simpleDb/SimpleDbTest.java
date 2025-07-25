package com.sbs.board.simpleDb;

import com.sbs.board.global.simpleDb.SimpleDb;
import com.sbs.board.global.simpleDb.Sql;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    int no = 1;
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

    // 들어온 번호가 1인지 확인
    assertThat(newId).isEqualTo(1L);
  }
}
