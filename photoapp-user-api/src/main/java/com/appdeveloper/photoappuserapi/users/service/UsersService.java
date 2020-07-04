package com.appdeveloper.photoappuserapi.users.service;

import com.appdeveloper.photoappuserapi.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDto createuser(UserDto userDto);
    UserDto getUserDetailsByEmail(String email);
    UserDto getUserByUserId(String userId);
}
