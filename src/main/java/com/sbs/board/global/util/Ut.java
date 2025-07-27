package com.sbs.board.global.util;

import java.util.HashMap;
import java.util.Map;

public class Ut {
  public static Map<String, String> getParamsFromUrl(String url) {
    Map<String, String> params = new HashMap<>();

    String[] urlBits = url.split("\\?", 2);

    if(urlBits.length == 1) return params;

    String queryStr = urlBits[1];

    for(String bit : queryStr.split("&")) {
      String[] bits = bit.split("=", 2);

      if(bits.length == 1) continue;

      params.put(bits[0], bits[1]);
    }

    return params;
  }

  public static String getPathFromUrl(String url) {
    return url.split("\\?", 2)[0];
  }
}
