package org.example.junmin.security;

import jakarta.annotation.Resource;
import org.example.junmin.dto.UserDTO;
import org.example.junmin.service.UserClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Resource
    private UserClient userClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userById = userClient.getUserById(username);
        if (userById == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(username,"{noop}"+userById.getPassword(),new ArrayList<>());
    }
}
