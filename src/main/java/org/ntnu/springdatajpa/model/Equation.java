package org.ntnu.springdatajpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Equation {
  @Id
  @GeneratedValue
  private Long id;
  private String username;
  private String equation;
  private String answer;

  public Equation(String username, String equation, String answer) {
    this.username = username;
    this.equation = equation;
    this.answer = answer;
  }
  public Equation() {}

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEquation() {
    return equation;
  }

  public void setEquation(String equation) {
    this.equation = equation;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
