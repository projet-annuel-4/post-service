package com.example.postservice.controller;


import com.example.postservice.domain.mapper.PostMapper;
import com.example.postservice.domain.mapper.UserMapper;
import com.example.postservice.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/post/user")
public class UserController {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getById(@PathVariable @NotNull Long userId){
        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(UserMapper.entityToModel(user.get()), HttpStatus.OK);
    }

    @GetMapping("/firstname/{firstname}")
    public ResponseEntity<?> getByFirstname(@PathVariable @NotNull String firstname){
        var user = userService.getByFirstname(firstname);
        if(user == null){
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
