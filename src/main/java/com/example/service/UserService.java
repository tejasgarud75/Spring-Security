package com.example.service;


import com.example.entity.Authority;
import com.example.entity.User;
import com.example.errors.BadRequestAlertException;
import com.example.errors.UserNotFoundException;
import com.example.repository.UserRepository;
import com.example.security.AuthoritiesConstants;
import com.example.security.jwt.JWTFilter;
import com.example.security.jwt.TokenProvider;
import com.example.service.dto.UserDTO;
import com.example.service.mapper.UserMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class UserService {

  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper mapper;

  public UserService(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
       PasswordEncoder passwordEncoder, UserRepository userRepository,
      UserMapper mapper) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  public Map<String, Object> authorize(@RequestParam String userName,
      @RequestParam String password) throws BadRequestAlertException {

    User user = userRepository.findByUserName(userName)
        .orElseThrow(() -> new BadRequestAlertException("Invalid username " + userName));

    if (!user.getActive()) {
      throw new BadRequestAlertException("User is not active " + userName);
    }

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userName, password)
    );

    // Generate JWT token
    String token = tokenProvider.createToken(authentication, user);

    String role = user.getAuthorities().stream().map(i -> i.getAuthority())
        .collect(Collectors.joining(","));

    // Create response map
    Map<String, Object> response = new HashMap<>();
    response.put("token", token);
    response.put("role", role);

    return response;

  }

  public UserDTO save(UserDTO userDTO) throws BadRequestAlertException {
    userDTO.setUserName(userDTO.getEmail());
    if (userRepository.existsByUserNameOrEmail(userDTO.getUserName(), userDTO.getEmail())) {
      throw new BadRequestAlertException("User name and email already exist");
    }
    User entity = mapper.toEntity(userDTO);
    entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    entity.setAuthorities(Set.of(new Authority(AuthoritiesConstants.USER)));  // Set authority as user
    return mapper.toDto(userRepository.save(entity));
  }

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new SecurityException("No authenticated user found");
    }

    String username = authentication.getName();
    return userRepository.findByUserName(username)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
  }

}
