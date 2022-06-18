package com.app.springbootcrud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "First Name shouldn't be empty")
    @Size(max = 30, message = "Length of firstName should be less than 30 characters")
    private String fullName;
    private String dob;
    @NotEmpty(message = "Email shouldn't be empty")
    @Email
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "Phone number shouldn't be empty")
    private String phoneNumber;
    @Column(nullable = true, length = 64)
    private String profileImage;
    private boolean active;


    @Transient
    public String getPhotosImagePath() {
        if (profileImage == null || id == null) return null;
        return "/pictures/" + id + "/" + profileImage;
    }

}
