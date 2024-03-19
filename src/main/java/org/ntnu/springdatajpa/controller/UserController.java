package org.ntnu.springdatajpa.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.ntnu.springdatajpa.model.User;
import org.ntnu.springdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {
  public static final String keyStr = "testsecrettestsecrettestsecrettestsecrettestsecret";
  private final UserService userService;
  boolean isLoggedIn = false;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  @CrossOrigin
  public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
    System.out.println(username + password);
    if (userService.getUserRepository().findUserByUsername(username) != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
    }
    userService.addUser(new User(username, password));
    System.out.println("New user registered");
    if (!isLoggedIn) {
      isLoggedIn = true;

      String accessToken = generateToken(username, Duration.ofMinutes(5));
      String refreshToken = generateToken(username, Duration.ofMinutes(30));
      Map<String, String> tokens = new HashMap<>();
      tokens.put("accessToken", accessToken);
      tokens.put("refreshToken", refreshToken);

      return ResponseEntity.ok(tokens);
    }
    else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already logged in");
    }
  }

  @PostMapping("/login")
  @CrossOrigin
  public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
    User user = userService.getUserRepository().findUserByUsername(username);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
    }
    if (!isLoggedIn) {
      if(Objects.equals(password, user.getPassword())) {
        isLoggedIn = true;
        System.out.println("User logged in");

        String accessToken = generateToken(username, Duration.ofMinutes(5));
        String refreshToken = generateToken(username, Duration.ofMinutes(30));
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
      }
      else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
      }
    }
    else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already logged in");
    }
  }

  @PostMapping("/refreshToken")
  @CrossOrigin
  public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
    System.out.println("Du har nådd refreshToken metode");
    try {
      Algorithm algorithm = Algorithm.HMAC512(keyStr);
      JWTVerifier verifier = JWT.require(algorithm).build(); // Reuse the JWTVerifier
      DecodedJWT jwt = verifier.verify(refreshToken); // Verify the passed refresh token
      String userId = jwt.getSubject();
      System.out.println(userId);

      // Assuming the refresh token is valid, issue a new access token
      String newAccessToken = generateToken(userId, Duration.ofMinutes(5));
      System.out.println("newAccessToken: " + newAccessToken);

      return ResponseEntity.ok(newAccessToken);
    } catch (JWTVerificationException exception){
      // Token is invalid
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
  }


  @PostMapping("/logoutUser")
  @CrossOrigin
  public ResponseEntity<?> logout() {
    isLoggedIn = false;
    System.out.println("Logged out");
    return ResponseEntity.ok("Logged out");
  }


  public String generateToken(final String userId, final Duration validMinutes) {
    final Instant now = Instant.now();
    final Algorithm hmac512 = Algorithm.HMAC512(keyStr);;
    final JWTVerifier verifier = JWT.require(hmac512).build();
    return JWT.create()
      .withSubject(userId)
      .withIssuer("idatt2105_token_issuer_app")
      .withIssuedAt(now)
      .withExpiresAt(now.plusMillis(validMinutes.toMillis()))
      .sign(hmac512);
  }
}
