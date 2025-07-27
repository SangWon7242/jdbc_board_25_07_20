package com.sbs.board.bundedContext.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article {
  private long id;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private String title;
  private String content;
}
