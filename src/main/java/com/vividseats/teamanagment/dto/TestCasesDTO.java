package com.vividseats.teamanagment.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TestCasesDTO {

  private List<TeamTestCaseDTO> testCase = new ArrayList<TeamTestCaseDTO>();

}
