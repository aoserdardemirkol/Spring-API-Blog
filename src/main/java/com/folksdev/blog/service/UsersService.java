package com.folksdev.blog.service;

import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.dto.converter.UsersDtoConverter;
import com.folksdev.blog.dto.request.CreateUsersRequest;
import com.folksdev.blog.dto.request.UpdateUsersRequest;
import com.folksdev.blog.exception.UsersNotFoundException;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersDtoConverter usersDtoConverter;

    public UsersService(UsersRepository usersRepository,
                        UsersDtoConverter usersDtoConverter){
        this.usersRepository = usersRepository;
        this.usersDtoConverter = usersDtoConverter;
    }

    public List<UsersDto> getAllUsers() {
        return usersDtoConverter.convertUsersDtoList(usersRepository.findAll());
    }

    public UsersDto getUsersById(String id){
        return usersDtoConverter.convert(findUsersById(id));
    }

    public UsersDto createUsers(CreateUsersRequest createUsersRequest){
        Users users = new Users(
                createUsersRequest.getUsername(),
                createUsersRequest.getGender()
        );
        return usersDtoConverter.convert(usersRepository.save(users));
    }

    public UsersDto updateUsers(String id, UpdateUsersRequest updateUsersRequest) {

        Users users = findUsersById(id);

        Users updatedUsers = new Users(
                users.getId(),
                updateUsersRequest.getUsername(),
                updateUsersRequest.getGender()
        );

        return usersDtoConverter.convert(usersRepository.save(updatedUsers));
    }

    public void deleteUserById(String id){
        findUsersById(id);
        usersRepository.deleteById(id);
    }

    protected Users findUsersById(String id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new UsersNotFoundException("User could not find by id:" + id));
    }
}
