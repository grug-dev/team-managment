package com.vividseats.teamanagment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;
import com.vividseats.teamanagment.dto.MemberDTO;
import com.vividseats.teamanagment.dto.TeamDTO;

/**
 * Finds the celebrity of a team. A celebrity knows nobody, and every other member knows the
 * celebrity.
 */
@Component("celebrityFinder")
public class CelebrityFinderServiceImpl implements TeamMemberFinderService {

  @Override
  public Optional<MemberDTO> findOf(TeamDTO team) {
    if (team == null || !team.hasMembers()) {
      return Optional.empty();
    }

    List<MemberDTO> members = new ArrayList<>(team.getMembers());
    MemberDTO candidate = findCandidate(members);

    return isCelebrity(candidate, members) ? Optional.of(candidate) : Optional.empty();
  }

  /**
   * Returns the only member who can still be the celebrity. If the candidate knows another
   * member, the candidate is not the celebrity. If not, that other member is not the celebrity.
   */
  private MemberDTO findCandidate(List<MemberDTO> members) {
    MemberDTO candidate = members.get(0);
    for (MemberDTO member : members) {
      if (knows(candidate, member)) {
        candidate = member;
      }
    }
    return candidate;
  }

  private boolean isCelebrity(MemberDTO candidate, List<MemberDTO> members) {
    if (knowsAnybody(candidate)) {
      return false;
    }
    for (MemberDTO member : members) {
      if (member != candidate && !knows(member, candidate)) {
        return false;
      }
    }
    return true;
  }

  private boolean knowsAnybody(MemberDTO member) {
    Set<Long> peopleKnown = member.getPeopleKnown();
    return peopleKnown != null && !peopleKnown.isEmpty();
  }

  private boolean knows(MemberDTO member, MemberDTO other) {
    Set<Long> peopleKnown = member.getPeopleKnown();
    return peopleKnown != null && peopleKnown.contains(other.getId());
  }

}
