package com.vividseats.teamanagment.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import com.vividseats.teamanagment.dto.MemberDTO;
import com.vividseats.teamanagment.dto.TeamDTO;
import com.vividseats.teamanagment.service.CelebrityFinderServiceImpl;
import com.vividseats.teamanagment.service.TeamMemberFinderService;

public class CelebrityFinderServiceTests {

  private TeamMemberFinderService celebrityFinder;

  @Before
  public void setup() {
    celebrityFinder = new CelebrityFinderServiceImpl();
  }

  private MemberDTO member(long id, long... peopleKnown) {
    Set<Long> known = new HashSet<>();
    for (long knownId : peopleKnown) {
      known.add(knownId);
    }
    return new MemberDTO(id, known);
  }

  private TeamDTO team(MemberDTO... members) {
    TeamDTO team = new TeamDTO();
    for (MemberDTO member : members) {
      team.add(member);
    }
    return team;
  }

  @Test
  public void given_TeamWithOneCelebrity_Then_ShouldBeFound() {
    TeamDTO team = team(member(1), member(2, 1), member(3, 1, 2), member(4, 1, 3),
        member(5, 1, 2, 3), member(6, 1, 4, 5));

    Optional<MemberDTO> celebrity = celebrityFinder.findOf(team);

    assertTrue(celebrity.isPresent());
    assertEquals(Long.valueOf(1), celebrity.get().getId());
  }

  @Test
  public void given_TeamWithoutACelebrity_Then_ShouldReturnEmptyMember() {
    TeamDTO team = team(member(2, 3), member(3, 4, 2), member(4, 2, 3), member(5, 3),
        member(6, 4, 5));

    assertFalse(celebrityFinder.findOf(team).isPresent());
  }

  @Test
  public void given_IdsBiggerThan127_Then_ShouldFindTheCelebrity() {
    TeamDTO team = team(member(1000), member(1001, 1000), member(1002, 1000, 1001));

    Optional<MemberDTO> celebrity = celebrityFinder.findOf(team);

    assertTrue(celebrity.isPresent());
    assertEquals(Long.valueOf(1000), celebrity.get().getId());
  }

  @Test
  public void given_MemberWhoKnowsNobodyButIsNotKnownByEveryone_Then_ShouldReturnEmpty() {
    TeamDTO team = team(member(1), member(2, 1), member(3, 2));

    assertFalse(celebrityFinder.findOf(team).isPresent());
  }

  @Test
  public void given_MemberWhoKnowsOnlyHimself_Then_ShouldReturnEmpty() {
    TeamDTO team = team(member(1, 1), member(2, 1), member(3, 1));

    assertFalse(celebrityFinder.findOf(team).isPresent());
  }

  @Test
  public void given_SingleMemberTeam_Then_ThatMemberIsTheCelebrity() {
    Optional<MemberDTO> celebrity = celebrityFinder.findOf(team(member(7)));

    assertTrue(celebrity.isPresent());
    assertEquals(Long.valueOf(7), celebrity.get().getId());
  }

  @Test
  public void given_EmptyTeam_Then_ShouldReturnEmptyCelebrity() {
    assertFalse(celebrityFinder.findOf(new TeamDTO()).isPresent());
  }

  @Test
  public void given_NullTeam_Then_ShouldReturnEmptyCelebrity() {
    assertFalse(celebrityFinder.findOf(null).isPresent());
  }

}
