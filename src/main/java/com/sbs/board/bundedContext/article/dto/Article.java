package com.sbs.board.bundedContext.article.dto;

public class Article {
  public int id;
  public String title;
  public String subject;

  public Article(int id, String title, String subject) {
    this.id = id;
    this.title = title;
    this.subject = subject;
  }

  @Override
  public String toString() {
    return "Article{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", subject='" + subject + '\'' +
        '}';
  }
}
