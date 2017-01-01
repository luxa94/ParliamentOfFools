package com.bdzjn.xml.service;

import com.bdzjn.xml.controller.dto.LoginDTO;
import com.bdzjn.xml.controller.dto.RegisterDTO;
import com.bdzjn.xml.controller.exception.AuthenticationException;
import com.bdzjn.xml.model.User;
import com.bdzjn.xml.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User register(RegisterDTO registerDTO) {
        final User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setRole(registerDTO.getRole());

        return userRepository.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User login(LoginDTO loginDTO) {
        final User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(AuthenticationException::new);
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new AuthenticationException();
        }
        return user;
    }
}
