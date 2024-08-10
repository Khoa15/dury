package com.example.apidury.repository;

import com.example.apidury.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository
    extends MongoRepository<Category, String> {
}
