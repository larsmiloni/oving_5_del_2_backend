package org.ntnu.springdatajpa.service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.ntnu.springdatajpa.model.Equation;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

  private final EquationRepository equationRepository;

  public CalculatorService(EquationRepository equationRepository) {
    this.equationRepository = equationRepository;
  }

  public String calculate(String equation) throws Exception {
    try {
      Expression expression = new ExpressionBuilder(equation).build();
      return String.valueOf(expression.evaluate());
    } catch (Exception e) {
      throw new Exception("Error evaluating the equation: " + e.getMessage());
    }
  }

  public Equation addEquation(Equation equation) {
    return equationRepository.save(equation);
  }

  public EquationRepository getEquationRepository() {
    return equationRepository;
  }
}
