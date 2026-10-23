package com.vividseats.teamanagment.dto;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TeamTestCaseDTO {

  private long idTeam;

  @JsonIgnore
  private Set<MemberDTO> memberDTO = new HashSet<>();

  private Long celebrity = -1L;

  private String message;

}
