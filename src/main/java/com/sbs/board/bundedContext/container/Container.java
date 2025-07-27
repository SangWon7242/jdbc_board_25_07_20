package com.sbs.board.bundedContext.container;

import com.sbs.board.bundedContext.article.controller.ArticleController;

import java.util.Scanner;

public class Container {
  public static Scanner sc;

  public static ArticleController articleController;

  static {
    sc = new Scanner(System.in);

    articleController = new ArticleController();
  }
}
