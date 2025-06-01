package com.example.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.errors.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {

    objectMapper.registerModule(new JavaTimeModule());

    // Create the ExceptionResponse object
    ExceptionResponse exceptionResponse = new ExceptionResponse(
        LocalDateTime.now(),
        HttpServletResponse.SC_FORBIDDEN,
        "Unauthorized",
        accessDeniedException.getMessage(),
        request.getRequestURI()
    );

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
  }
}
