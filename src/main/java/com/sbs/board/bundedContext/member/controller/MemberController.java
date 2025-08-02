package com.sbs.board.bundedContext.member.controller;


import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.bundedContext.member.service.MemberService;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.Sql;

import com.sbs.board.bundedContext.member.dto.Member;
import java.util.ArrayList;
import java.util.List;

public class MemberController implements Controller {
  private MemberService memberService;

  public MemberController() {
    memberService = Container.memberService;
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
    Member member;

    System.out.println("== 회원 가입 ===");

    // username 입력
    while (true) {
      System.out.print("아이디 : ");
      username = Container.sc.nextLine();

      if(username.trim().isEmpty()) {
        System.out.println("아이디를 입력해주세요.");
        continue;
      }

      member = memberService.findByUsername(username);

      if(member != null) {
        System.out.println("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
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

    memberService.create(username, password, name);

    System.out.println("회원이 가입되었습니다.");
  }
}
