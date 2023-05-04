package com.example.final_project.repositories;

import com.example.final_project.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
//    com.example.springsecurityapplication.models.Category findByName(String name);
}
