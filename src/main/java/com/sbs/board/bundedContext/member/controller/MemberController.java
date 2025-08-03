package com.sbs.board.bundedContext.member.controller;


import com.sbs.board.bundedContext.common.controller.Controller;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.bundedContext.member.dto.Member;
import com.sbs.board.bundedContext.member.service.MemberService;
import com.sbs.board.global.base.Rq;

public class MemberController implements Controller {
  private MemberService memberService;

  public MemberController() {
    memberService = Container.memberService;
  }

  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/member/join" -> doJoin(rq);
      case "/usr/member/login" -> doLogin(rq);
      case "/usr/member/logout" -> doLogout(rq);
      case "/usr/member/mypage" -> showMyPage(rq);
      default -> System.out.println("알 수 없는 명령어입니다.");
    }
  }

  private void showMyPage(Rq rq) {
    if(!rq.isLogined()) {
      System.out.println("로그인 상태가 아닙니다.");
      return;
    }

    Member member = rq.getLoginedMember();

    System.out.println("== 마이페이지 ===");
    System.out.printf("아이디 : %s\n", member.getUsername());
    System.out.printf("이름 : %s\n", member.getName());
    System.out.printf("가입일 : %s\n", member.getFormatRegDate());
  }

  private void doLogout(Rq rq) {
    if(!rq.isLogined()) {
      System.out.println("로그인 상태가 아닙니다.");
      return;
    }

    rq.logout();

    System.out.println("로그아웃 되었습니다.");
  }

  private void doLogin(Rq rq) {
    if(rq.isLogined()) {
      System.out.println("이미 로그인 상태입니다.");
      return;
    }

    String username;
    String password;
    Member member;

    System.out.println("== 로그인 ===");

    // username 입력
    while (true) {
      System.out.print("아이디 : ");
      username = Container.sc.nextLine();

      if(username.trim().isEmpty()) {
        System.out.println("아이디를 입력해주세요.");
        continue;
      }

      member = memberService.findByUsername(username);

      if(member == null) {
        System.out.println("아이디가 존재하지 않습니다. 회원가입을 먼저 해주세요.");
        continue;
      }

      break;
    }
  
    // 비밀번호 입력 횟수 담당 로직
    int tryMaxCount = 3;
    int tryCount = 0;

    // password 입력
    while (true) {
      if(tryCount >= tryMaxCount) {
        System.out.println("비밀번호 입력 횟수를 초과했습니다. 확인후 입력해주세요.");
        return;
      }

      System.out.print("비밀번호 : ");
      password = Container.sc.nextLine();

      if(password.trim().isEmpty()) {
        System.out.println("비밀번호를 입력해주세요.");
        continue;
      }

      if(!member.getPassword().equals(password)) {
        tryCount++;
        System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        System.out.printf("비밀번호 틀린 횟수 (%d / %d)\n", tryCount, tryMaxCount);
        continue;
      }

      break;
    }

    // 세션에 로그인 데이터 저장
    rq.login(member);

    System.out.println("로그인 되었습니다.");
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
