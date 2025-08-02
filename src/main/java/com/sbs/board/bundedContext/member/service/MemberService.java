package com.sbs.board.bundedContext.member.service;

import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.bundedContext.member.dto.Member;
import com.sbs.board.bundedContext.member.repository.MemberRepository;

public class MemberService {
  private MemberRepository memberRepository;

  public MemberService() {
    memberRepository = Container.memberRepository;
  }

  public Member findByUsername(String username) {
    return memberRepository.findByUsername(username);
  }

  public void create(String username, String password, String name) {
    memberRepository.save(username, password, name);
  }
}
