package com.ecommerce.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String username;
    private String password;
    private String mobileNumber;
    private String email;
    private String role;
    private LocalDate dateOfCreation = LocalDate.now();

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
}