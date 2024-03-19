package org.ntnu.springdatajpa.service;

import org.ntnu.springdatajpa.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
  User findUserByUsername(String username);
}
