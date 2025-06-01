package com.example.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.errors.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      org.springframework.security.core.AuthenticationException authException)
      throws IOException {

    objectMapper.registerModule(new JavaTimeModule());

    // Create the ExceptionResponse object
    ExceptionResponse exceptionResponse = new ExceptionResponse(
        LocalDateTime.now(),
        HttpServletResponse.SC_UNAUTHORIZED,
        "Unauthorized",
        authException.getMessage(),
        request.getRequestURI()
    );

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
  }

}
