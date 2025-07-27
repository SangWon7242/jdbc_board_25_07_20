package com.sbs.board.bundedContext.article.dto;

import com.sbs.board.bundedContext.common.BaseDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Article extends BaseDto {
  private String title;
  private String content;
}
