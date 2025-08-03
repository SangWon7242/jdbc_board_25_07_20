package com.sbs.board.bundedContext.app;

import com.sbs.board.bundedContext.article.controller.ArticleController;
import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.SimpleDb;

public class App {
  private SimpleDb simpleDb;
  private ArticleController articleController;

  public App() {
    simpleDb = new SimpleDb("localhost", "sbsst", "sbs123414", "JDBC_board");
    simpleDb.setDevMode(true); // 개발 모드 활성화 (디버깅을 위해 SQL 쿼리 출력)

    Container.init(simpleDb);

    articleController = Container.articleController;
  }

  public void run() {
    System.out.println("== 게시판 프로그램 시작 ==");

    while (true) {
      Rq rq = new Rq();

      String promptName = "명령어";

      if(rq.isLogined()) {
        promptName = "%s님".formatted(rq.getLoginedMember().getUsername());
      }

      System.out.printf("%s) ", promptName);

      String cmd = Container.sc.nextLine().trim();

      rq.setCommand(cmd);

      Controller controller = getControllerByUrl(rq.getUrlPath());

      if(controller != null) {
        controller.performAction(rq);
      } else if (cmd.equals("exit")) {
        System.out.println("프로그램을 종료합니다.");
        break;
      }
    }

    System.out.println("== 게시판 프로그램 끝 =");
    Container.sc.close();
  }

  private Controller getControllerByUrl(String urlPath) {
    if( urlPath.startsWith("/usr/article")) {
      return articleController;
    }
    else if( urlPath.startsWith("/usr/member")) {
      return Container.memberController;
    }

    return null;
  }
}
