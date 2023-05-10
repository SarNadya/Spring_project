package com.example.final_project.repositories;

import com.example.final_project.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository <Person, Integer> {
    Optional<Person> findByLogin(String login);

    @Modifying
    @Query("UPDATE Person person SET person.role = ?2 WHERE person.id = ?1")
    int changePersonRole(int id, String role);
}
