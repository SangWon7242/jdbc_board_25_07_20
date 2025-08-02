package com.sbs.board.bundedContext.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public abstract class BaseDto {
  protected long id;
  protected LocalDateTime regDate;
  protected LocalDateTime updateDate;

  public String getFormatRegDate() {
    return regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public String getFormatUpdateDate() {
    return updateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
