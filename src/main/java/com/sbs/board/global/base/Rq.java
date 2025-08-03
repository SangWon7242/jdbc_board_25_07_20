package com.sbs.board.global.base;

import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.bundedContext.member.dto.Member;
import com.sbs.board.global.session.Session;
import com.sbs.board.global.simpleDb.SimpleDb;
import com.sbs.board.global.simpleDb.Sql;
import com.sbs.board.global.util.Ut;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Rq {
  private String url;

  @Getter
  private Map<String, String> params;

  @Getter
  private String urlPath;

  private Session session;
  private String loginedMember = "loginedMember";

  @Getter
  @Setter
  private String controllerTypeCode;

  @Getter
  @Setter
  private String controllerName;

  @Getter
  @Setter
  private String actionMethodName;

  public Rq() {
    session = Container.session;
  }

  public String getActionPath() {
    String[] commandBits = urlPath.split("/");

    if (commandBits.length < 4) {
      return null;
    }
    // /usr/article/list
    // [, "usr", "article", "list"]
    controllerTypeCode = commandBits[1];
    controllerName = commandBits[2];
    actionMethodName = commandBits[3];

    return "/%s/%s/%s".formatted(controllerTypeCode, controllerName, actionMethodName);
  }

  public void setCommand(String url) {
    this.url = url;
    params = Ut.getParamsFromUrl(this.url);
    urlPath = Ut.getPathFromUrl(this.url);
  }

  public long getLongParam(String paramName, long defaultValue) {
    if (!params.containsKey(paramName)) {
      return defaultValue;
    }

    try {
      return Long.parseLong(params.get(paramName));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public String getParam(String paramName, String defaultValue) {
    if (!params.containsKey(paramName)) return defaultValue;

    return params.get(paramName);
  }

  // 로그인 여부 확인
  public boolean isLogined() {
    return hasSessionAttr(loginedMember);
  }

  // 세션 관련 메소드
  public void setSessionAttr(String key, Object value) {
    session.setAttribute(key, value);
  }

  public Object getSessionAttr(String key) {
    return session.getAttribute(key);
  }

  public boolean hasSessionAttr(String key) {
    return session.hasAttribute(key);
  }

  public void removeSessionAttr(String key) {
    session.removeAttribute(key);
  }

  public void login(Object value) {
    setSessionAttr(loginedMember, value);
  }

  public void logout() {
    removeSessionAttr(loginedMember);
  }

  public Member getLoginedMember() {
    return (Member) getSessionAttr(loginedMember);
  }
}
