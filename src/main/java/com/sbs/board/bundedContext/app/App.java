package com.sbs.board.bundedContext.app;

import com.sbs.board.bundedContext.article.controller.ArticleController;
import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class App {
  private Scanner sc;
  private SimpleDb simpleDb;
  private ArticleController articleController;

  public App() {
    sc = Container.sc;

    simpleDb = new SimpleDb("localhost", "sbsst", "sbs123414", "JDBC_board");
    simpleDb.setDevMode(true); // 개발 모드 활성화 (디버깅을 위해 SQL 쿼리 출력)

    articleController = Container.articleController;
  }

  public void run() {
    System.out.println("== 게시판 프로그램 시작 ==");

    while (true) {
      Rq rq = new Rq();

      System.out.print("명령어) ");
      String cmd = sc.nextLine().trim();

      rq.setCommand(cmd, simpleDb);

      Controller controller = getControllerByUrl(rq.getUrlPath());

      if(controller != null) {
        controller.performAction(rq);
      } else if (cmd.equals("exit")) {
        System.out.println("프로그램을 종료합니다.");
        break;
      }
    }

    System.out.println("== 게시판 프로그램 끝 =");
    sc.close();
  }

  private Controller getControllerByUrl(String urlPath) {
    if( urlPath.startsWith("/usr/article")) {
      return articleController;
    }

    return null;
  }
}
