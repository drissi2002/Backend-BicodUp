package com.exam.backend.controller;


import com.exam.backend.helper.UserFoundException;
import com.exam.backend.model.Role;
import com.exam.backend.model.User;
import com.exam.backend.model.UserRole;
import com.exam.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Creating user
    @PostMapping("/")
    public User createUser(@RequestBody User user) throws UserFoundException {
        user.setProfile("default.png");
        //encoding password with bcryptpasswordEncoder

        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        Set<UserRole> roles=new HashSet<>();
        Role role =new Role();
        role.setRoleId(45L);
        role.setRoleName("NORMAL");

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        roles.add(userRole);

        return this.userService.createUser(user,roles);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username){
        return this.userService.getUser(username);
    }

    //delete user by id
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId){
        this.userService.deleteUser(userId);
    }

    //update api
    @ExceptionHandler(UserFoundException.class)
    public void exceptionHandler(UserFoundException ex){
        return  ;
    }

}
