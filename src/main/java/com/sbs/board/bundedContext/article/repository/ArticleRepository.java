package com.sbs.board.bundedContext.article.repository;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.simpleDb.Sql;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {
  private List<Article> articles;

  public ArticleRepository() {
    articles = new ArrayList<>();
  }

  public long save(String title, String content, long memberId) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", title = ?", title);
    sql.append(", content = ?", content);
    sql.append(", memberId = ?", memberId);

    long id = sql.insert();

    return id;
  }

  public List<Article> findByOrderByIdDesc() {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT A.*");
    sql.append(", M.username AS writerName");
    sql.append("FROM article A");
    sql.append("INNER JOIN `member` M");
    sql.append("ON A.memberId = M.id");
    sql.append("ORDER BY A.id DESC");

    List<Article> articles = sql.selectRows(Article.class);

    if(articles == null) return null;

    return articles;
  }

  public Article findById(long id) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT A.*");
    sql.append(", M.username AS writerName");
    sql.append("FROM article A");
    sql.append("INNER JOIN `member` M");
    sql.append("ON A.memberId = M.id");
    sql.append("WHERE A.id = ?", id);

    Article article = sql.selectRow(Article.class);

    if(article == null) return null;

    return article;
  }

  public void update(long id, String title, String content) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("UPDATE article");
    sql.append("SET updateDate = NOW()");
    sql.append(", title = ?", title);
    sql.append(", content = ?", content);
    sql.append("WHERE id = ?", id);

    sql.update();
  }

  public void delete(long id) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("DELETE FROM article");
    sql.append("WHERE id = ?", id);

    sql.delete();
  }

  public List<Article> findByContainsSearchKeyword(String searchKeyword, String searchType) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT A.*");
    sql.append(", M.username AS writerName");
    sql.append("FROM article A");
    sql.append("INNER JOIN `member` M");
    sql.append("ON A.memberId = M.id");

    if(searchKeyword.trim().isEmpty() || searchType.trim().isEmpty()) {
      return null;
    } else if(searchType.equals("title")) {
      sql.append("WHERE A.title LIKE CONCAT('%', ?, '%')", searchKeyword);
    } else if(searchType.equals("content")) {
      sql.append("WHERE A.content LIKE CONCAT('%', ?, '%')", searchKeyword);
    } else if(searchType.equals("title,content")) {
      sql.append("WHERE A.title LIKE CONCAT('%', ?, '%')", searchKeyword);
      sql.append("OR A.content LIKE CONCAT('%', ?, '%')", searchKeyword);
    }

    sql.append("ORDER BY A.id DESC");

    List<Article> articles = sql.selectRows(Article.class);

    if(articles == null) return null;

    return articles;
  }
}
