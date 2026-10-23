package com.vividseats.teamanagment.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import com.vividseats.teamanagment.dto.MemberDTO;
import com.vividseats.teamanagment.dto.TeamTestCaseDTO;
import com.vividseats.teamanagment.dto.TestCasesDTO;
import com.vividseats.teamanagment.exceptions.FilterCelebrityUploadException;

/**
 * Reads the uploaded text file and builds the test cases.
 *
 * Format: the first line has the number of test cases T. Each test case starts with a line
 * "teamId membersCount". Then each member has one line: "memberId knownId knownId ...". Blank
 * lines are ignored.
 */
@Component
public class TestCasesFileParser {

  /** Parses the input. The caller owns the stream and must close it. */
  public TestCasesDTO parse(InputStream input) throws IOException {
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    String firstLine = nextLine(reader);
    if (firstLine == null) {
      throw new FilterCelebrityUploadException("The number of test cases is missing");
    }
    int numberOfTestCases = parseCount(singleToken(firstLine), "number of test cases");

    TestCasesDTO testCases = new TestCasesDTO();
    for (int i = 1; i <= numberOfTestCases; i++) {
      testCases.getTestCase().add(parseTestCase(reader, i));
    }
    return testCases;
  }

  private TeamTestCaseDTO parseTestCase(BufferedReader reader, int testCaseNumber)
      throws IOException {
    String header = nextLine(reader);
    if (header == null) {
      throw new FilterCelebrityUploadException(
          String.format("Test case %d is missing", testCaseNumber));
    }
    String[] tokens = tokens(header);
    if (tokens.length != 2) {
      throw new FilterCelebrityUploadException(String.format(
          "Test case %d must start with the team id and the number of members", testCaseNumber));
    }

    TeamTestCaseDTO testCase = new TeamTestCaseDTO();
    testCase.setIdTeam(parseId(tokens[0], "team id"));
    int membersCount = parseCount(tokens[1], "number of members");

    for (int i = 1; i <= membersCount; i++) {
      String line = nextLine(reader);
      if (line == null) {
        throw new FilterCelebrityUploadException(
            String.format("Team %d expects %d members but only %d were found",
                testCase.getIdTeam(), membersCount, i - 1));
      }
      MemberDTO member = parseMember(line);
      if (!testCase.getMemberDTO().add(member)) {
        throw new FilterCelebrityUploadException(String.format(
            "Member %d appears twice in team %d", member.getId(), testCase.getIdTeam()));
      }
    }
    return testCase;
  }

  private MemberDTO parseMember(String line) {
    String[] tokens = tokens(line);
    MemberDTO member = new MemberDTO();
    member.setId(parseId(tokens[0], "member id"));
    for (int i = 1; i < tokens.length; i++) {
      member.getPeopleKnown().add(parseId(tokens[i], "known member id"));
    }
    return member;
  }

  /** Returns the next line that is not blank, or null at the end of the file. */
  private String nextLine(BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      if (!line.trim().isEmpty()) {
        return line;
      }
    }
    return null;
  }

  private String[] tokens(String line) {
    return line.trim().split("\\s+");
  }

  private String singleToken(String line) {
    String[] tokens = tokens(line);
    if (tokens.length != 1) {
      throw new FilterCelebrityUploadException(
          "The first line must only have the number of test cases");
    }
    return tokens[0];
  }

  private long parseId(String value, String name) {
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new FilterCelebrityUploadException(
          String.format("The %s '%s' is not a number", name, value));
    }
  }

  private int parseCount(String value, String name) {
    int count;
    try {
      count = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new FilterCelebrityUploadException(
          String.format("The %s '%s' is not a number", name, value));
    }
    if (count < 0) {
      throw new FilterCelebrityUploadException(
          String.format("The %s '%s' can not be negative", name, value));
    }
    return count;
  }

}
