package com.vividseats.teamanagment.dto;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TeamDTO {
  @JsonIgnore
  private Long id;

  private String name;

  @JsonIgnore
  private Set<MemberDTO> members = new HashSet<>();


  public void add(MemberDTO member) {
    members.add(member);
  }

  public boolean hasMembers() {
    return members != null && !members.isEmpty();
  }

}
