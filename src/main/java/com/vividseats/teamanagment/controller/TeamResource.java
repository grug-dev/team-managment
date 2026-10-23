package com.vividseats.teamanagment.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vividseats.teamanagment.dto.MemberDTO;
import com.vividseats.teamanagment.dto.TeamDTO;
import com.vividseats.teamanagment.dto.TeamTestCaseDTO;
import com.vividseats.teamanagment.dto.TestCasesDTO;
import com.vividseats.teamanagment.exceptions.TestCasesFileNotFoundException;
import com.vividseats.teamanagment.parser.TestCasesFileParser;
import com.vividseats.teamanagment.service.TeamMemberFinderService;

@RestController
@RequestMapping("/v1/teams")
public class TeamResource {

  private final TestCasesFileParser parser;
  private final TeamMemberFinderService finderService;

  public TeamResource(TestCasesFileParser parser, TeamMemberFinderService finderService) {
    this.parser = parser;
    this.finderService = finderService;
  }

  @PostMapping("/upload/filterByCelebrity")
  public ResponseEntity<TestCasesDTO> uploadFile(@RequestParam("file") MultipartFile uploadfile)
      throws IOException {
    if (uploadfile.isEmpty()) {
      throw new TestCasesFileNotFoundException("File wasn't uploaded");
    }

    TestCasesDTO testCases;
    try (InputStream input = uploadfile.getInputStream()) {
      testCases = parser.parse(input);
    }

    testCases.getTestCase().forEach(this::solve);

    return ResponseEntity.ok(testCases);
  }

  private void solve(TeamTestCaseDTO testCase) {
    TeamDTO team = new TeamDTO();
    team.setId(testCase.getIdTeam());
    team.setMembers(testCase.getMemberDTO());

    Optional<MemberDTO> celebrity = finderService.findOf(team);
    if (celebrity.isPresent()) {
      testCase.setCelebrity(celebrity.get().getId());
      testCase.setMessage("Celebrity Found with ID: " + celebrity.get().getId());
    } else {
      testCase.setMessage("Celebrity not found");
    }
  }

}
