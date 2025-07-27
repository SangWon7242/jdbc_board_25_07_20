package com.sbs.board.bundedContext.common.dto;

import java.time.LocalDateTime;

public abstract class BaseDto {
  protected long id;
  protected LocalDateTime regDate;
  protected LocalDateTime updateDate;
}
