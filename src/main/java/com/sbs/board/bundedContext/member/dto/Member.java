package com.sbs.board.bundedContext.member.dto;

import com.sbs.board.bundedContext.common.dto.BaseDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member extends BaseDto {
  private String username;
  private String password;
  private String name;
}
