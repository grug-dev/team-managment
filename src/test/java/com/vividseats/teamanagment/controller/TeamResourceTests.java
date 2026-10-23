package com.vividseats.teamanagment.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.vividseats.teamanagment.TeamManagmentApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamManagmentApplication.class)
@AutoConfigureMockMvc
public class TeamResourceTests {

  private static final String URI_UPLOAD = "/v1/teams/upload/filterByCelebrity";

  private static final String SAMPLE =
      "2\n1 5\n1 \n2 1 3\n3 1 4\n4 1 2\n5 1 3\n2 3\n1 1\n2 1\n3 1";

  @Autowired
  private MockMvc mockMvc;

  private MockMultipartFile file(String content) {
    return new MockMultipartFile("file", "data.txt", "text/plain",
        content.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void given_SampleFile_Then_ShouldReturnTheCelebrityOfEachTeam() throws Exception {
    mockMvc.perform(multipart(URI_UPLOAD).file(file(SAMPLE)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.testCase", hasSize(2)))
        .andExpect(jsonPath("$.testCase[0].idTeam").value(1))
        .andExpect(jsonPath("$.testCase[0].celebrity").value(1))
        .andExpect(jsonPath("$.testCase[0].message").value("Celebrity Found with ID: 1"))
        .andExpect(jsonPath("$.testCase[1].idTeam").value(2))
        .andExpect(jsonPath("$.testCase[1].celebrity").value(-1))
        .andExpect(jsonPath("$.testCase[1].message").value("Celebrity not found"));
  }

  @Test
  public void given_EmptyFile_Then_ShouldReturnBadRequest() throws Exception {
    mockMvc.perform(multipart(URI_UPLOAD).file(file(""))).andExpect(status().isBadRequest());
  }

  @Test
  public void given_InvalidFile_Then_ShouldReturnBadRequest() throws Exception {
    mockMvc.perform(multipart(URI_UPLOAD).file(file("1\n1 x\n1")))
        .andExpect(status().isBadRequest());
  }

}
