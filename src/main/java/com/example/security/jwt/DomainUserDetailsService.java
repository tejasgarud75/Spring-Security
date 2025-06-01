package com.example.security.jwt;


import com.example.entity.User;
import com.example.errors.BadRequestAlertException;
import com.example.errors.UserNotFoundException;
import com.example.repository.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DomainUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public DomainUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(email)
        .orElseThrow(() -> new UsernameNotFoundException("email not found"));

    if (!user.getActive()) {
      throw new UsernameNotFoundException("User is deactivated");
    }

    return getAuthentication(user);
  }

  public static org.springframework.security.core.userdetails.User getAuthentication(User user) {
    Set<GrantedAuthority> authorities = user.getAuthorities()
        .stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
        .collect(Collectors.toSet());
    return new org.springframework.security.core.userdetails.User(
        user.getUserName(),
        user.getPassword(),
        authorities);
  }
}
