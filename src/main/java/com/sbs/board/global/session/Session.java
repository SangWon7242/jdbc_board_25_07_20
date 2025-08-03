package com.sbs.board.global.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
  private Map<String, Object> sessionStorage;

  public Session() {
    sessionStorage = new HashMap<>();
  }

  // C : 생성, R : 조회, 세션유무, D : 삭제
  public void setAttribute(String key, Object value) {
    sessionStorage.put(key, value);
  }

  public Object getAttribute(String key) {
    return sessionStorage.get(key);
  }

  public boolean hasAttribute(String key) {
    return sessionStorage.containsKey(key);
  }

  public void removeAttribute(String key) {
    sessionStorage.remove(key);
  }
}
