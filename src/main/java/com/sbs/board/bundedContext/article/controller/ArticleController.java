package com.sbs.board.bundedContext.article.controller;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.article.service.ArticleService;
import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.bundedContext.member.dto.Member;
import com.sbs.board.global.base.Rq;

import java.util.List;

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
    if (!rq.isLogined()) {
      System.out.println("로그인 상태가 아닙니다.");
      return;
    }

    long id = rq.getLongParam("id", 0);

    if (id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Article article = articleService.findById(id);

    if (article == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    articleService.delete(id);

    System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);
  }

  private void doModify(Rq rq) {
    if (!rq.isLogined()) {
      System.out.println("로그인 상태가 아닙니다.");
      return;
    }

    long id = rq.getLongParam("id", 0);

    if (id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Article article = articleService.findById(id);

    if (article == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.println("== 게시글 수정 ==");
    System.out.print("제목 : ");
    String title = Container.sc.nextLine();

    if (title == null || title.trim().isEmpty()) {
      System.out.println("제목을 입력해주세요.");
      return;
    }

    System.out.print("내용 : ");
    String content = Container.sc.nextLine();

    if (content == null || content.trim().isEmpty()) {
      System.out.println("내용을 입력해주세요.");
      return;
    }

    articleService.update(id, title, content);

    System.out.printf("%d번 게시글이 수정되었습니다.\n", id);
  }

  private void showDetail(Rq rq) {
    long id = rq.getLongParam("id", 0);

    if (id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Article article = articleService.findById(id);

    if (article == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.println("== 게시글 상세 ==");
    System.out.printf("번호 : %d\n", article.getId());
    System.out.printf("제목 : %s\n", article.getTitle());
    System.out.printf("내용 : %s\n", article.getContent());
    System.out.printf("작성일 : %s\n", article.getFormatRegDate());
    System.out.printf("수정일 : %s\n", article.getFormatUpdateDate());
    System.out.printf("작성자 : %s\n", article.getWriterName());
  }

  public void doWrite(Rq rq) {
    if (!rq.isLogined()) {
      System.out.println("로그인 상태가 아닙니다.");
      return;
    }

    System.out.println("== 게시글 작성 ==");

    System.out.print("제목 : ");
    String title = Container.sc.nextLine();

    System.out.print("내용 : ");
    String content = Container.sc.nextLine();

    Member member = rq.getLoginedMember();
    long memberId = member.getId();

    long id = articleService.create(title, content, memberId);

    System.out.printf("%d번 게시물이 작성되었습니다.\n", id);
  }

  public void showList(Rq rq) {
    String searchKeyword = rq.getParam("searchKeyword", "");
    String searchType = rq.getParam("searchType", "");

    List<Article> articles = articleService.findByOrderByIdDesc(searchKeyword, searchType);

    if (articles == null || articles.isEmpty()) {
      System.out.println("게시글이 존재하지 않습니다.");
      return;
    }

    System.out.println("== 게시글 목록 ==");
    System.out.println("번호 | 제목 | 작성일 | 작성자");
    System.out.println("-------------------");
    articles.forEach(article -> {
      long id = article.getId();
      String title = article.getTitle();
      String regDate = article.getFormatRegDate();
      String writerName = article.getWriterName();


      System.out.printf("%d | %s | %s | %s\n", id, title, regDate, writerName);
    });
  }
}
