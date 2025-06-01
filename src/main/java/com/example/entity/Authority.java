package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {

  @Id
  private String name;

  @Override
  public String getAuthority() {
    return name;
  }

  public Authority(String name) {
    this.name = name;
  }

  public Authority() {
  }
}