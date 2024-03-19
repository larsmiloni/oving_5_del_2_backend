package org.ntnu.springdatajpa.service;

import org.ntnu.springdatajpa.model.Equation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EquationRepository extends CrudRepository<Equation, Long> {
  List<Equation> findTop10ByUsernameOrderByIdDesc(String username);
}
