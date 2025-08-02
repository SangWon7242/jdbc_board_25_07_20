package com.sbs.board.bundedContext.container;

import com.sbs.board.bundedContext.article.controller.ArticleController;
import com.sbs.board.bundedContext.article.repository.ArticleRepository;
import com.sbs.board.bundedContext.article.service.ArticleService;
import com.sbs.board.bundedContext.member.controller.MemberController;
import com.sbs.board.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class Container {
  public static Scanner sc;
  public static SimpleDb simpleDb;

  public static ArticleRepository articleRepository;

  public static ArticleService articleService;

  public static MemberController memberController;
  public static ArticleController articleController;

  public static void init(SimpleDb dbInfo) {
    sc = new Scanner(System.in);
    simpleDb = dbInfo;

    articleRepository = new ArticleRepository();

    articleService = new ArticleService();

    memberController = new MemberController();
    articleController = new ArticleController();
  }
}
