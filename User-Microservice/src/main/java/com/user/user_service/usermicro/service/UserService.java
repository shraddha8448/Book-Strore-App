package com.user.user_service.usermicro.service;

import com.user.user_service.usermicro.dto.LoginDTO;
import com.user.user_service.usermicro.dto.UserDTO;

public interface UserService {

    UserDTO registerUser(UserDTO userDTO);

    String login(LoginDTO loginDTO);

    String forgotPassword(String email);

    String resetPassword(String token, String newPassword);

    UserDTO getUser(String token);

    String removeUser(String token);

}
