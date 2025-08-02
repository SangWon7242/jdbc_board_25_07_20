package com.sbs.board.bundedContext.member.controller;


import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.Sql;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class MemberController implements Controller {
  private List<Member> members;

  public MemberController() {
    members = new ArrayList<>();
  }

  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/member/join" -> doJoin(rq);
      default -> System.out.println("알 수 없는 명령어입니다.");
    }
  }

  private void doJoin(Rq rq) {
    String username;
    String password;
    String passwordConfirm;
    String name;


    System.out.println("== 회원 가입 ===");

    // username 입력
    while (true) {
      System.out.print("아이디 : ");
      username = Container.sc.nextLine();

      if(username.trim().isEmpty()) {
        System.out.println("아이디를 입력해주세요.");
        continue;
      }

      break;
    }

    // password 입력
    while (true) {
      System.out.print("비밀번호 : ");
      password = Container.sc.nextLine();

      if(password.trim().isEmpty()) {
        System.out.println("비밀번호를 입력해주세요.");
        continue;
      }

      while (true) {
        System.out.print("비밀번호 확인 : ");
        passwordConfirm = Container.sc.nextLine();

        if(passwordConfirm.trim().isEmpty()) {
          System.out.println("비밀번호 확인을 입력해주세요.");
          continue;
        }

        if(!passwordConfirm.equals(password)) {
          System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
          continue;
        }

        break;
      }
      
      break;
    }

    // name 입력
    while (true) {
      System.out.print("이름 : ");
      name = Container.sc.nextLine();

      if(name.trim().isEmpty()) {
        System.out.println("이름을 입력해주세요.");
        continue;
      }

      break;
    }

    // 회원 정보 저장
    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO `member`");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", username = ?", username);
    sql.append(", password = ?", password);
    sql.append(", name = ?", name);

    long id = sql.insert();

    System.out.printf("%d번 회원이 가입되었습니다.\n", id);
  }
}
