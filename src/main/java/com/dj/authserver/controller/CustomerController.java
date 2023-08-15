package com.dj.authserver.controller;

import com.dj.authserver.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerController(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder ) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {
        UserDetails newUser = User.withUsername(userRequest.getUsername())
                .password(userRequest.getPassword())
                .roles(userRequest.getRoles())
                .build();

        ((InMemoryUserDetailsManager) userDetailsService).createUser(newUser);
        return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserRequest userRequest) {

        if(((InMemoryUserDetailsManager) userDetailsService).userExists(userRequest.getUsername())) {
            UserDetails updateUser = User.withUsername(userRequest.getUsername())
                    .password(userRequest.getPassword())
                    .roles(userRequest.getRoles())
                    .build();

            ((InMemoryUserDetailsManager) userDetailsService).updateUser(updateUser);
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        }
        else {
            UserDetails newUser = User.withUsername(userRequest.getUsername())
                    .password(userRequest.getPassword())
                    .roles(userRequest.getRoles())
                    .build();

            ((InMemoryUserDetailsManager) userDetailsService).createUser(newUser);
            return new ResponseEntity<>("User updated successfully", HttpStatus.CREATED);
        }
    }

    @DeleteMapping
    public void deleteUser(@PathVariable String username) {

        ((InMemoryUserDetailsManager) userDetailsService).deleteUser(username);

    }

}