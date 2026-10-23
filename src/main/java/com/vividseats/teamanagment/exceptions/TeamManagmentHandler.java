package com.vividseats.teamanagment.exceptions;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TeamManagmentHandler {

  @ExceptionHandler(value = {TeamNotFoundException.class, MemberNotFoundException.class})
  public ResponseEntity<Object> handleUserNotFound(Exception ex, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setDate(LocalDate.now());
    exceptionResponse.setMessage(ex.getMessage() + " - " + request.getDescription(false));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
  }

  @ExceptionHandler(value = {MemberCreatedException.class, FilterCelebrityUploadException.class,
      TestCasesFileNotFoundException.class})
  public ResponseEntity<Object> handleCreationMember(Exception ex, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setDate(LocalDate.now());
    exceptionResponse.setMessage(ex.getMessage() + " - " + request.getDescription(false));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setDate(LocalDate.now());
    exceptionResponse.setMessage(ex.getMessage() + " - " + request.getDescription(false));

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
  }

}
