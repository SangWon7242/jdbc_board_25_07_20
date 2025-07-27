package com.sbs.board.bundedContext.article.controller;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.Sql;

import java.util.ArrayList;
import java.util.List;

public class ArticleController {
  private List<Article> articles;

  public ArticleController() {
    articles = new ArrayList<>();
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
}
