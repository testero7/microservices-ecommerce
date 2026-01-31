package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.UserDTO;
import com.ecommerce.userservice.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);
    User updateUser(UserDTO userDTO, Integer userId);
    User getUserById(Integer userId);
    List<User> getAllUsers();
    void deleteUser(Integer userId);
    User getUserByMobileNumber(String mobileNumber);
}