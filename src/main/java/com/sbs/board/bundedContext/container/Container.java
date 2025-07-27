package com.sbs.board.bundedContext.container;

import com.sbs.board.bundedContext.article.controller.ArticleController;
import com.sbs.board.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class Container {
  public static Scanner sc;
  public static SimpleDb simpleDb;

  public static ArticleController articleController;

  public static void init(SimpleDb dbInfo) {
    sc = new Scanner(System.in);
    simpleDb = dbInfo;

    articleController = new ArticleController();
  }
}
