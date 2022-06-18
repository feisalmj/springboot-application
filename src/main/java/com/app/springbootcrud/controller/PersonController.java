package com.app.springbootcrud.controller;

import com.app.springbootcrud.model.Person;
import com.app.springbootcrud.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Controller
public class PersonController {
    @Autowired
    PersonService personService;

    //---------------Get all users section---------------------------//
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("personsList", personService.getAllUsers());
        return "index";
    }

    //---------------Add User section---------------------------//

    @GetMapping("/person/add")
    public String addUser(Model model) {
        model.addAttribute("person", new Person());
        return "add-person";
    }

    @PostMapping("/person/add")
    public String addUser(@Valid Person person, BindingResult bindingResult, @RequestParam("image") MultipartFile multipartFile, Model model) {
        Optional<Person> existingPerson = personService.personWithEmail(person.getEmail());

        if (multipartFile.getSize() > 1000000) {
            model.addAttribute("fileSizeGreater", true);
            return "add-person";
        }

        if (existingPerson.isPresent()) {
            bindingResult.rejectValue("email", "error.email", "Email already exists");
            return "add-person";
        }

        if (LocalDate.parse(person.getDob()).isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dob", "error.dob", "Date of birth should be before today's date");
            return "add-person";
        }

        if (!person.getPhoneNumber().matches("^(\\+\\d{1,3}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{3}$")) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Phone number should be formatted like +255 712 345 678");
            return "add-person";
        }

        if (!bindingResult.hasErrors()) {
            personService.saveUser(person, multipartFile);
            return "redirect:/";
        }

        return "add-person";
    }

    //---------------Delete User section---------------------------//

    @GetMapping("/person/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer userId) {
        personService.deleteUser(userId);
        return "redirect:/";
    }

    //---------------Update User section---------------------------//
    @GetMapping("/person/update/{id}")
    public String updateUser(@PathVariable(name = "id") Integer userId, Model model) {
        model.addAttribute("person", personService.getUserById(userId));
        return "update-person";
    }

    @PostMapping("/person/update")
    public String updateUser(@Valid Person person, BindingResult bindingResult, @RequestParam("image") MultipartFile multipartFile, Model model) {

        if (multipartFile.getSize() > 1000000) {
            model.addAttribute("fileSizeGreater", true);
            return "add-person";
        }
        Optional<Person> existingPerson = personService.personWithEmail(person.getEmail());
        if (existingPerson.isPresent() && !Objects.equals(existingPerson.get().getId(), person.getId())) {
            bindingResult.rejectValue("email", "error.email", "This Email already exists");
            return "add-person";
        }

        if (LocalDate.parse(person.getDob()).isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dob", "error.dob", "Date of birth should be before today's date");
            return "update-person";
        }

        if (!person.getPhoneNumber().matches("^(\\+\\d{1,3}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{3}$")) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Phone number should be formatted like +255 712 345 678");
            return "update-person";
        }

        if (!bindingResult.hasErrors()) {
            personService.saveUser(person, multipartFile);
        }
        return "redirect:/";
    }

    //---------------Search User section---------------------------//
    @GetMapping("/person/search")
    public String searchUserByName(@RequestParam(name = "keyword") String keyword, Model model) {
        model.addAttribute("personsList", personService.getAllPersonsByName(keyword));
        return "index";
    }

}
