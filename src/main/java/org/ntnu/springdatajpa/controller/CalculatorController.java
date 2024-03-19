package org.ntnu.springdatajpa.controller;

import org.ntnu.springdatajpa.model.Equation;
import org.ntnu.springdatajpa.service.CalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

  private final CalculatorService calculatorService;

  public CalculatorController(CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  @PostMapping("/calculate")
  @CrossOrigin
  public ResponseEntity<?> calculate(@RequestParam String username, @RequestParam String equation) throws Exception {
    String result = calculatorService.calculate(equation);
    calculatorService.addEquation(new Equation(username, equation, result));
    System.out.println("Equation successfully added to DB");
    return ResponseEntity.ok(result);
  }

  @PostMapping("/getUsersEquations")
  @CrossOrigin
  public ResponseEntity<?> calculate(@RequestParam String username) {
    System.out.println("Getting users equations");
    return ResponseEntity.ok(calculatorService.getEquationRepository().findTop10ByUsernameOrderByIdDesc(username));
  }
}
