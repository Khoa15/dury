package com.example.apidury.controller;

import com.example.apidury.model.Note;
import com.example.apidury.model.User;
import com.example.apidury.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> loadAll(){
        return userService.loadAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable("id") String userId){
        return userService.get(userId);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        try{
            return ResponseEntity.ok(userService.createUser(user));
        }catch(Exception e){
            throw new RuntimeException("Your username is already used!");
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        try{
            return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
