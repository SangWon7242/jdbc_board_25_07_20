package com.sbs.board.bundedContext.article.dto;

public class Article {
  public long id;
  public String title;
  public String content;

  public Article(long id, String title, String content) {
    this.id = id;
    this.title = title;
    this.content = content;
  }

  @Override
  public String toString() {
    return "Article{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
