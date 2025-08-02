package com.sbs.board.bundedContext.member.repository;

import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.bundedContext.member.dto.Member;
import com.sbs.board.global.simpleDb.Sql;

import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
  private List<Member> members;

  public MemberRepository() {
    members = new ArrayList<>();
  }

  public Member findByUsername(String username) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM member");
    sql.append("WHERE username = ?", username);

    Member member = sql.selectRow(Member.class);

    if(member == null) return null;

    return member;
  }

  public void save(String username, String password, String name) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO `member`");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", username = ?", username);
    sql.append(", password = ?", password);
    sql.append(", name = ?", name);

    sql.insert();
  }
}
