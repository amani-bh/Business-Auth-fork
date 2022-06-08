package com.courzelo_for_business.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.courzelo_for_business.auth.entities.Business;

public interface UserRepository extends MongoRepository<Business, String> {
  Optional<Business> findByEmail(String email);
  Business findByIdBusiness(String idBusiness);
  List<Business> findByActive(boolean active);
  Boolean existsByCompanyName(String companyName);
  Boolean existsByEmail(String email);

  
}
