package com.vividseats.teamanagment.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Component
@Scope("prototype")
public class MemberDTO {

  private Long id;
  @JsonIgnore
  private TeamDTO team;
  @JsonIgnore
  private Set<MemberDTO> knows = new HashSet<>();
  private Set<Long> peopleKnown = new HashSet<>();

  public MemberDTO() {

  }

  public MemberDTO(Long memberId, TeamDTO team, Set<Long> membersKnown) {
    this.id = memberId;
    this.team = team;
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
