package com.courzelo_for_business.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.courzelo_for_business.auth.entities.ERole;
import com.courzelo_for_business.auth.entities.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
