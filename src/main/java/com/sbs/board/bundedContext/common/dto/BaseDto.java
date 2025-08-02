package com.sbs.board.bundedContext.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseDto {
  protected long id;
  protected LocalDateTime regDate;
  protected LocalDateTime updateDate;
}
