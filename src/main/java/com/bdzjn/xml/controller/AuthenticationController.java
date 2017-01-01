package com.bdzjn.xml.controller;


import com.bdzjn.xml.controller.dto.LoginDTO;
import com.bdzjn.xml.controller.dto.RegisterDTO;
import com.bdzjn.xml.model.User;
import com.bdzjn.xml.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody RegisterDTO registerDTO) {
        if (userService.isUsernameTaken(registerDTO.getUsername())) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        final User user = userService.register(registerDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        final User user = userService.login(loginDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
