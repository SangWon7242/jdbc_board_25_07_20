package com.sbs.board.bundedContext.article.controller;

import com.sbs.board.bundedContext.article.service.ArticleService;
import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;

import java.util.List;
import java.util.Map;

public class ArticleController implements Controller {
  private ArticleService articleService;

  public ArticleController() {
    articleService = Container.articleService;
  }

  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/article/write" -> doWrite(rq);
      case "/usr/article/list" -> showList(rq);
      case "/usr/article/detail" -> showDetail(rq);
      case "/usr/article/modify" -> doModify(rq);
      case "/usr/article/delete" -> deDelete(rq);
      default -> System.out.println("알 수 없는 명령어입니다.");
    }
  }

  private void deDelete(Rq rq) {
    long id = rq.getLongParam("id", 0);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Map<String, Object> articleRow = articleService.findById(id);

    if(articleRow == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    articleService.delete(id);

    System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);
  }

  private void doModify(Rq rq) {
    long id = rq.getLongParam("id", 0);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Map<String, Object> articleRow = articleService.findById(id);

    if(articleRow == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.println("== 게시글 수정 ==");
    System.out.print("제목 : ");
    String title = Container.sc.nextLine();

    if(title == null || title.trim().isEmpty()) {
      System.out.println("제목을 입력해주세요.");
      return;
    }

    System.out.print("내용 : ");
    String content = Container.sc.nextLine();

    if(content == null || content.trim().isEmpty()) {
      System.out.println("내용을 입력해주세요.");
      return;
    }

    articleService.update(id, title, content);

    System.out.printf("%d번 게시글이 수정되었습니다.\n", id);
  }

  private void showDetail(Rq rq) {
    long id = rq.getLongParam("id", 0);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Map<String, Object> articleRow = articleService.findById(id);

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

    long id = articleService.create(title, content);

    System.out.printf("%d번 게시물이 작성되었습니다.\n", id);
  }

  public void showList(Rq rq) {
    List<Map<String, Object>> articleRows = articleService.findByOrderByIdDesc();

    if(articleRows == null) {
      System.out.println("게시글이 존재하지 않습니다.");
      return;
    }

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
