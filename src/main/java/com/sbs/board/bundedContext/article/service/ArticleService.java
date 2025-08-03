package com.sbs.board.bundedContext.article.service;

import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.article.repository.ArticleRepository;
import com.sbs.board.bundedContext.container.Container;

import java.util.List;

public class ArticleService {
  private ArticleRepository articleRepository;

  public ArticleService() {
    articleRepository = Container.articleRepository;
  }

  public long create(String title, String content, long memberId) {
    return articleRepository.save(title, content, memberId);
  }

  public List<Article> findByContainsSearchKeyword(String searchKeyword, String searchType) {
    return articleRepository.findByContainsSearchKeyword(searchKeyword, searchType);
  }

  public List<Article> findByOrderByIdDesc(String searchKeyword, String searchType) {
    if(searchKeyword != null && !searchKeyword.isEmpty()) {
      return findByContainsSearchKeyword(searchKeyword, searchType);
    }

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
