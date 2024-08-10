package com.example.apidury.repository;


import com.example.apidury.model.ERole;
import com.example.apidury.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
