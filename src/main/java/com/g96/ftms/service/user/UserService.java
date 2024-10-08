package com.g96.ftms.service.user;

import com.g96.ftms.dto.JwtResponeDTO;
import com.g96.ftms.dto.LoginDTO;
import com.g96.ftms.dto.UserDTO;
import com.g96.ftms.entity.User;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface UserService {
    User findByAccount(String account);
    List<UserDTO> getAllUsers();
    JwtResponeDTO login(LoginDTO loginDTO);
    void saveUser(UserDTO userDTO);
    UserDTO getUserProfileByAccount(String account);
}

