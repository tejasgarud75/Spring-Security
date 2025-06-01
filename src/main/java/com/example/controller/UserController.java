package com.example.controller;

import com.example.entity.ApiResponse;
import com.example.errors.BadRequestAlertException;
import com.example.service.UserService;
import com.example.service.dto.UserDTO;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/authenticate")
  public ApiResponse authorize(
      @RequestParam String userName,
      @RequestParam String password) throws BadRequestAlertException {

    Map<String, Object> token = userService.authorize(userName, password);

    return new ApiResponse<>(
        HttpStatus.OK.value(),
        "Authentication successful",
        token
    );

  }

  @Transactional
  @PostMapping("/user")
  public ApiResponse create(@RequestBody UserDTO userDTO)
      throws BadRequestAlertException {
    if (userDTO.getId() != null) {
      throw new BadRequestAlertException("A new user cannot already have an ID");
    }
    UserDTO savedUser = userService.save(userDTO);
    return new ApiResponse(
        HttpStatus.OK.value(),
        "User created successfully",
        savedUser);
  }

}

