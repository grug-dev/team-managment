package com.vividseats.teamanagment.exceptions;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TeamManagmentHandler {

  @ExceptionHandler(
      value = {FilterCelebrityUploadException.class, TestCasesFileNotFoundException.class})
  public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
  }

  private ResponseEntity<Object> buildResponse(HttpStatus status, Exception ex,
      WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setDate(LocalDate.now());
    exceptionResponse.setMessage(ex.getMessage() + " - " + request.getDescription(false));

    return ResponseEntity.status(status).body(exceptionResponse);
  }

}
