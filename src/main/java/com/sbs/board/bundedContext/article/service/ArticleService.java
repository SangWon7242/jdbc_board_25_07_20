package com.sbs.board.bundedContext.article.service;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.article.repository.ArticleRepository;
import com.sbs.board.bundedContext.container.Container;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArticleService {
  private ArticleRepository articleRepository;

  public ArticleService() {
    articleRepository = Container.articleRepository;
  }

  public long create(String title, String content) {
    return articleRepository.save(title, content);
  }

  public List<Article> findByOrderByIdDesc() {
    return articleRepository.findByOrderByIdDesc();
  }

  public Article findById(long id) {
    return articleRepository.findById(id);
  }

  public void update(long id, String title, String content) {
    articleRepository.update(id, title, content);
  }

  public void delete(long id) {
    articleRepository.delete(id);
  }
}
