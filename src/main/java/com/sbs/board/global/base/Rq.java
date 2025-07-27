package com.sbs.board.global.base;

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

  @Getter
  @Setter
  private String controllerTypeCode;

  @Getter
  @Setter
  private String controllerName;

  @Getter
  @Setter
  private String actionMethodName;

  public String getActionPath() {
    String[] commandBits = urlPath.split("/");

    if(commandBits.length < 4) {
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
}
