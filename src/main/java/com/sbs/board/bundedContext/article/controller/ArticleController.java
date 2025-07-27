package com.sbs.board.bundedContext.article.controller;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleController implements Controller {
  private List<Article> articles;

  public ArticleController() {
    articles = new ArrayList<>();
  }

  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/article/write" -> doWrite(rq);
      case "/usr/article/list" -> showList(rq);
      case "/usr/article/detail" -> showDetail(rq);
      default -> System.out.println("알 수 없는 명령어입니다.");
    }
  }

  private void showDetail(Rq rq) {
    long id = rq.getLongParam("id", 0);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Sql sql = rq.sql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = ?", id);

    Map<String, Object> articleRow = sql.selectRow();

    if(articleRow == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.println("== 게시글 상세 ==");
    System.out.printf("번호 : %d\n", (long) articleRow.get("id"));
    System.out.printf("제목 : %s\n",  articleRow.get("title"));
    System.out.printf("내용 : %s\n",  articleRow.get("content"));
    System.out.printf("작성일 : %s\n", articleRow.get("regDate"));
    System.out.printf("수정일 : %s\n", articleRow.get("updateDate"));

  }

  public void doWrite(Rq rq) {
    System.out.println("== 게시글 작성 ==");

    System.out.print("제목 : ");
    String title = Container.sc.nextLine();

    System.out.print("내용 : ");
    String content = Container.sc.nextLine();

    Sql sql = rq.sql();
    sql.append("INSERT INTO article");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", title = ?", title);
    sql.append(", content = ?", content);

    long id = sql.insert();

    System.out.printf("%d번 게시물이 작성되었습니다.\n", id);
  }

  public void showList(Rq rq) {
    Sql sql = rq.sql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id DESC");

    List<Map<String, Object>> articleRows = sql.selectRows();

    System.out.println("== 게시글 목록 ==");
    System.out.println("번호 | 제목 | 작성일");
    System.out.println("-------------------");
    articleRows.forEach(articleRow -> {
      long id = (long) articleRow.get("id");
      String title = (String) articleRow.get("title");
      String regDate = articleRow.get("regDate").toString();

      System.out.printf("%d | %s | %s\n", id, title, regDate);
    });
  }
}
