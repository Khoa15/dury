package com.example.apidury.repository;


import com.example.apidury.model.RefreshToken;
import com.example.apidury.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);


  int deleteByUser(User user);
}
