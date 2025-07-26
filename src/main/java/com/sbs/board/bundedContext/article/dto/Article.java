package com.sbs.board.bundedContext.article.dto;

import java.time.LocalDateTime;

public class Article {
  public long id;
  public LocalDateTime regDate;
  public LocalDateTime updateDate;
  public String title;
  public String content;

  public Article(long id, String title, String content) {
    this(id, LocalDateTime.now(), LocalDateTime.now(), title, content);
  }

  public Article(long id, LocalDateTime regDate, LocalDateTime updateDate, String title, String content) {
    this.id = id;
    this.regDate = regDate;
    this.updateDate = updateDate;
    this.title = title;
    this.content = content;
  }

  @Override
  public String toString() {
    return "Article{" +
        "id=" + id +
        ", regDate=" + regDate +
        ", updateDate=" + updateDate +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
