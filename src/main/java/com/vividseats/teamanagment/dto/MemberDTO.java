package com.vividseats.teamanagment.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Data
public class MemberDTO {

  private Long id;
  private Set<Long> peopleKnown = new HashSet<>();

  public MemberDTO() {

  }

  public MemberDTO(Long memberId, Set<Long> membersKnown) {
    this.id = memberId;
    this.peopleKnown = membersKnown;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    MemberDTO other = (MemberDTO) obj;
    return Objects.equals(id, other.id);
  }

}
