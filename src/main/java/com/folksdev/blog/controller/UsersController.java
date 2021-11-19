package com.folksdev.blog.controller;

import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.dto.request.CreateUsersRequest;
import com.folksdev.blog.dto.request.UpdateUsersRequest;
import com.folksdev.blog.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/v1/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService){
        this.usersService=usersService;
    }

    @GetMapping
    public ResponseEntity<List<UsersDto>> getUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(usersService.getUsersById(id));
    }

    @PostMapping
    public ResponseEntity<UsersDto> createUser(@Valid @RequestBody CreateUsersRequest usersRequest){
        return new ResponseEntity<>(usersService.createUsers(usersRequest), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDto> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUsersRequest usersRequest){
        return ResponseEntity.ok(usersService.updateUsers(id, usersRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        usersService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
