package com.example.demo.repository;

import com.example.demo.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByEmail (String email);
    Optional<UserModel> findByMobilePhone (String mobilePhone);

}
