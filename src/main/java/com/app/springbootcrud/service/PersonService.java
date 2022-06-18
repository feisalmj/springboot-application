package com.app.springbootcrud.service;

import com.app.springbootcrud.model.Person;
import com.app.springbootcrud.repository.PersonRepository;
import com.app.springbootcrud.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public Person saveUser(Person person, MultipartFile multipartFile) {
        try {
            person.setActive(true);
            if (multipartFile.isEmpty()) {
                return personRepository.save(person);
            }
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            person.setProfileImage(fileName);

            Person savedPerson = personRepository.save(person);

            String uploadDir = "pictures/" + savedPerson.getId();

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            return savedPerson;
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        }
    }

    public List<Person> getAllUsers() {
        try {
            return personRepository.findAllByActive(true);
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        }
    }

    public boolean deleteUser(Integer userId) {
        try {
            Optional<Person> user = personRepository.findById(userId);
            if (user.isPresent()) {
                user.get().setActive(false);
                personRepository.save(user.get());
                return true;
            } else {
                throw new RuntimeException("There is no user against this Id");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Person getUserById(Integer userId) {
        try {
            Optional<Person> user = personRepository.findById(userId);
            if (user.isPresent()) {
                return user.get();
            } else {
                throw new RuntimeException("There is no user against this Id");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<Person> getAllPersonsByName(String keyword) {
        return personRepository.findAllByFullNameContainingOrEmailContainingOrPhoneNumberContainingAndActive(keyword, keyword, keyword, true);
    }

    public Optional<Person> personWithEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }
}
