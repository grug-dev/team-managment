package com.vividseats.teamanagment.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.vividseats.teamanagment.dto.MemberDTO;
import com.vividseats.teamanagment.dto.TeamTestCaseDTO;
import com.vividseats.teamanagment.dto.TestCasesDTO;
import com.vividseats.teamanagment.exceptions.FilterCelebrityUploadException;

public class TestCasesFileParserTests {

  private static final String SAMPLE =
      "2\n1 5\n1 \n2 1 3\n3 1 4\n4 1 2\n5 1 3\n2 3\n1 1\n2 1\n3 1";

  private TestCasesFileParser parser;

  @Before
  public void setup() {
    parser = new TestCasesFileParser();
  }

  private TestCasesDTO parse(String content) throws IOException {
    return parser.parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
  }

  private MemberDTO findMember(TeamTestCaseDTO testCase, long id) {
    for (MemberDTO member : testCase.getMemberDTO()) {
      if (member.getId() == id) {
        return member;
      }
    }
    throw new AssertionError("Member " + id + " not found");
  }

  @Test
  public void given_SampleFile_Then_ShouldParseAllTestCases() throws IOException {
    List<TeamTestCaseDTO> testCases = parse(SAMPLE).getTestCase();

    assertEquals(2, testCases.size());
    assertEquals(1, testCases.get(0).getIdTeam());
    assertEquals(5, testCases.get(0).getMemberDTO().size());
    assertEquals(2, testCases.get(1).getIdTeam());
    assertEquals(3, testCases.get(1).getMemberDTO().size());
  }

  @Test
  public void given_SampleFile_Then_ShouldParseKnownMembers() throws IOException {
    TeamTestCaseDTO firstTeam = parse(SAMPLE).getTestCase().get(0);

    assertTrue(findMember(firstTeam, 1).getPeopleKnown().isEmpty());
    assertEquals(new HashSet<>(Arrays.asList(1L, 3L)), findMember(firstTeam, 2).getPeopleKnown());
  }

  @Test
  public void given_BlankLinesAndWindowsLineEndings_Then_ShouldIgnoreThem() throws IOException {
    TestCasesDTO testCases = parse("1\r\n\r\n7 2\r\n1\r\n\r\n2 1\r\n");

    assertEquals(1, testCases.getTestCase().size());
    assertEquals(7, testCases.getTestCase().get(0).getIdTeam());
    assertEquals(2, testCases.getTestCase().get(0).getMemberDTO().size());
  }

  @Test
  public void given_BigIds_Then_ShouldParseThem() throws IOException {
    TeamTestCaseDTO testCase = parse("1\n1 2\n1000\n1001 1000").getTestCase().get(0);

    assertEquals(new HashSet<>(Arrays.asList(1000L)), findMember(testCase, 1001).getPeopleKnown());
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_EmptyFile_Then_ShouldFail() throws IOException {
    parse("  \n");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_NumberOfTestCasesIsNotANumber_Then_ShouldFail() throws IOException {
    parse("two\n1 1\n1");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_NumberOfMembersIsNotANumber_Then_ShouldFail() throws IOException {
    parse("1\n1 x\n1");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_NegativeNumberOfMembers_Then_ShouldFail() throws IOException {
    parse("1\n1 -1");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_HeaderWithoutNumberOfMembers_Then_ShouldFail() throws IOException {
    parse("1\n1\n1");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_KnownMemberIdIsNotANumber_Then_ShouldFail() throws IOException {
    parse("1\n1 2\n1\n2 abc");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_MissingMemberLines_Then_ShouldFail() throws IOException {
    parse("1\n1 3\n1\n2 1");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_MissingTestCase_Then_ShouldFail() throws IOException {
    parse("2\n1 1\n1");
  }

  @Test(expected = FilterCelebrityUploadException.class)
  public void given_DuplicatedMember_Then_ShouldFail() throws IOException {
    parse("1\n1 2\n1\n1 2");
  }

}
