package com.app.springbootcrud.repository;

import com.app.springbootcrud.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> findAllByActive(boolean status);

    List<Person> findAllByFullNameContainingAndActive(String name, boolean active);

    Optional<Person> findPersonByEmail(String email);

    List<Person> findAllByFullNameContainingOrEmailContainingOrPhoneNumberContainingAndActive(String fullName,
                                                                                              String email,
                                                                                              String phoneNumber,
                                                                                              boolean active);
}
