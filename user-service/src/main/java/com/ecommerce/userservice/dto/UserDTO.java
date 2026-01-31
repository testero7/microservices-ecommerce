package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;

    @NotEmpty(message = "Nazwa użytkownika nie może być pusta")
    private String username;

    @NotEmpty(message = "Numer telefonu nie może być pusty")
    @Pattern(regexp = "48[0-9]{9}", message = "Invalid mobile number")
    private String mobileNumber;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Wprowadź silne hasło")
    private String password;

    @NotEmpty(message = "Email nie może być pusty")
    @Email(message = "Błędnie wprowadzony email")
    private String email;

    private String role;
}