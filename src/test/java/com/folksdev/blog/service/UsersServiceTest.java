package com.folksdev.blog.service;

import com.folksdev.blog.TestSupport;
import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.dto.converter.UsersDtoConverter;
import com.folksdev.blog.dto.request.CreateUsersRequest;
import com.folksdev.blog.dto.request.UpdateUsersRequest;
import com.folksdev.blog.exception.UsersNotFoundException;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsersServiceTest extends TestSupport {
    private UsersRepository usersRepository;
    private UsersDtoConverter usersDtoConverter;

    private UsersService usersService;

    @BeforeEach
    void setUp() {
        usersRepository = Mockito.mock(UsersRepository.class);
        usersDtoConverter = Mockito.mock(UsersDtoConverter.class);

        usersService = new UsersService(usersRepository, usersDtoConverter);
    }

    @Test
    void testGetAllUsers_shouldReturnListOfUsersDto(){
        List<Users> usersList = generateUsersList();
        List<UsersDto> usersDtoList = generateUsersDtoList();

        Mockito.when(usersRepository.findAll()).thenReturn(usersList);
        Mockito.when(usersDtoConverter.convertUsersDtoList(usersList)).thenReturn(usersDtoList);

        List<UsersDto> result = usersService.getAllUsers();

        assertEquals(usersDtoList, result);

        Mockito.verify(usersRepository).findAll();
        Mockito.verify(usersDtoConverter).convertUsersDtoList(usersList);
    }

    @Test
    void testGetUsersById_whenUsersIdExist_shouldReturnUsersDto() {
        Users users = generateUsers();
        UsersDto usersDto = generateUsersDto();

        Mockito.when(usersRepository.findById("userId")).thenReturn(Optional.of(users));
        Mockito.when(usersDtoConverter.convert(users)).thenReturn(usersDto);

        UsersDto result = usersService.getUsersById("userId");

        assertEquals(usersDto, result);

        Mockito.verify(usersRepository).findById("userId");
        Mockito.verify(usersDtoConverter).convert(users);
    }

    @Test
    void testGetUsersById_whenUsersIdNotExist_shouldThrowUsersNotFoundException(){
        Mockito.when(usersRepository.findById("userId")).thenThrow(UsersNotFoundException.class);

        assertThrows(UsersNotFoundException.class, () -> usersService.getUsersById("userId"));

        Mockito.verify(usersRepository).findById("userId");
        Mockito.verifyNoInteractions(usersDtoConverter);
    }

    @Test
    void testCreateUsers_whenCreateUsersRequestIsValid_shouldReturnUsersDto(){
        CreateUsersRequest usersRequest = generateCreateUsersRequest();
        Users users = generateUsers();
        UsersDto usersDto = generateUsersDto();

        Mockito.when(usersRepository.save(users)).thenReturn(users);
        Mockito.when(usersDtoConverter.convert(users)).thenReturn(usersDto);

        UsersDto result = usersService.createUsers(usersRequest);

        assertEquals(usersDto, result);

        Mockito.verify(usersRepository).save(users);
        Mockito.verify(usersDtoConverter).convert(users);
    }

    @Test
    void testUpdateUsers_whenUsersIdNotExist_shouldThrowUsersNotFoundException(){
        UpdateUsersRequest usersRequest = generateUpdateUsersRequest();

        Mockito.when(usersRepository.findById("userId")).thenThrow(UsersNotFoundException.class);

        assertThrows(UsersNotFoundException.class, () -> usersService.updateUsers("userId", usersRequest));

        Mockito.verify(usersRepository).findById("userId");
        Mockito.verifyNoInteractions(usersDtoConverter);
    }


    @Test
    void testUpdateUsers_whenUpdateRequest_shouldReturnUsersDto(){
        UpdateUsersRequest updateUsersRequest = generateUpdateUsersRequest();
        Users users = generateUsers();
        UsersDto usersDto = generateUsersDto();

        Mockito.when(usersRepository.findById("userId")).thenReturn(Optional.ofNullable(users));
        assert users != null;
        Mockito.when(usersRepository.save(users)).thenReturn(users);
        Mockito.when(usersDtoConverter.convert(users)).thenReturn(usersDto);

        UsersDto result = usersService.updateUsers("userId", updateUsersRequest);

        assertEquals(usersDto, result);

        Mockito.verify(usersRepository).findById("userId");
        Mockito.verify(usersRepository).save(users);
        Mockito.verify(usersDtoConverter).convert(users);
    }

    @Test
    void testFindUsersById_whenUsersIdExist_shouldReturnUsers(){
        Users users = generateUsers();

        Mockito.when(usersRepository.findById("userId")).thenReturn(Optional.of(users));

        Users result = usersService.findUsersById("userId");

        assertEquals(users, result);

        Mockito.verify(usersRepository).findById("userId");
    }

    @Test
    void testFindUsersById_whenUsersIdNotExist_shouldThrowUsersNotFoundException(){
        Mockito.when(usersRepository.findById("userId")).thenThrow(UsersNotFoundException.class);

        assertThrows(UsersNotFoundException.class, () -> usersService.findUsersById("userId"));

        Mockito.verify(usersRepository).findById("userId");
    }

    @Test
    void testDeleteUserById_whenUsersIdExist_shouldReturnVoid(){
        Users users = generateUsers();

        Mockito.when(usersRepository.findById("userId")).thenReturn(Optional.of(users));

        usersService.deleteUserById("userId");

        Mockito.verify(usersRepository).deleteById("userId");
    }

    @Test
    void testDeleteUserById_whenUsersIdNotExist_shouldThrowUsersNotFoundException(){
        Mockito.when(usersRepository.findById("userId")).thenThrow(UsersNotFoundException.class);

        assertThrows(UsersNotFoundException.class, () -> usersService.deleteUserById("userId"));

        Mockito.verify(usersRepository).findById("userId");
    }
}
