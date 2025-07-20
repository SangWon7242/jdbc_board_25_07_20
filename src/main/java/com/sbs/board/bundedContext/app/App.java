package com.sbs.board.bundedContext.app;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.container.Container;

import java.util.Scanner;

public class App {
  public Scanner sc;
  public int lastArticleId;

  public App() {
    sc = Container.sc;
    lastArticleId = 0;
  }

  public void run() {
    System.out.println("== 게시판 프로그램 시작 ==");

    while (true) {
      System.out.print("명령어) ");
      String cmd = sc.nextLine().trim();

      if(cmd.equals("/usr/article/write")) {
        System.out.println("== 게시글 작성 ==");

        System.out.print("제목 : ");
        String title = sc.nextLine();

        System.out.print("내용 : ");
        String subject = sc.nextLine();

        int id = ++lastArticleId;

        Article article = new Article(id, title, subject);
        System.out.println("생성 된 게시물 객체 : " + article);
        System.out.printf("%d번 게시물이 작성되었습니다.\n", id);
      } else if (cmd.equals("exit")) {
        System.out.println("프로그램을 종료합니다.");
        break;
      } else {
        System.out.println("알 수 없는 명령어입니다.");
      }
    }

    System.out.println("== 게시판 프로그램 끝 =");
    sc.close();
  }
}
