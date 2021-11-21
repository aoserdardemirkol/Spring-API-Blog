package com.folksdev.blog.controller;

import com.folksdev.blog.IntegrationTestSupport;
import com.folksdev.blog.dto.request.CreateUsersRequest;
import com.folksdev.blog.dto.request.UpdateUsersRequest;
import com.folksdev.blog.model.Gender;
import com.folksdev.blog.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UsersControllerIT extends IntegrationTestSupport {

    public String Url = "/v1/users/";

    @Test
    public void testGetAllUsers_whenRequestIsValid_shouldReturnEmptyList() throws Exception{
        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Users> usersList = usersRepository.findAll();
        assertEquals(0, usersList.size());
    }

    @Test
    public void testGetAllUsers_whenRequestIsValid_shouldReturnListOfUsersDto() throws Exception{
        Users users = generateUsers();
        usersRepository.save(users);

        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Users> usersList = usersRepository.findAll();
        assertEquals(1, usersList.size());
    }

    @Test
    public void testGetUsersById_whenUsersIdNotExist_shouldReturnNotFound() throws Exception {

        this.mockMvc.perform(get(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUsersById_whenUsersIdIsExist_shouldReturnUsersDto() throws Exception{
        Users users = usersRepository.save(generateUsers());

        this.mockMvc.perform(get(Url + users.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(users.getId())))
                .andExpect(jsonPath("$.username", is(users.getUsername())))
                .andExpect(jsonPath("$.gender", is(users.getGender().name())));
    }

    @Test
    public void testCreateUsers_whenCreateUsersRequestIsInvalid_shouldNotCreateUsersAndReturnBadRequest() throws Exception{
        CreateUsersRequest usersRequest = new CreateUsersRequest(
                "",
                Gender.MALE
        );

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(usersRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", notNullValue()));

        List<Users> createdUsers = usersRepository.findAll();

        assertEquals(0, createdUsers.size());
    }

    @Test
    public void testCreateUsers_whenCreateUsersRequestIsValid_shouldCreateUsersAndReturnUsersDto() throws Exception{
        CreateUsersRequest usersRequest = generateCreateUsersRequest();

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(usersRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("username")));

        List<Users> createdUsers = usersRepository.findAll();

        assertEquals(1, createdUsers.size());
    }

    @Test
    public void testDeleteUsersById_whenIdIsExist_shouldDeleteUsersAndReturnVoid() throws Exception {
        Users users = usersRepository.save(generateUsers());

        this.mockMvc.perform(delete(Url + users.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteUsersById_whenIdNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(delete(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUsers_whenIdAndRequestIsValid_shouldUpdateUsersAndReturnUsersDto() throws Exception {
        Users users = usersRepository.save(generateUsers());
        UpdateUsersRequest request = generateUpdateUsersRequest();
        Users updatedUser = generateUpdatedUser(users, request);

        this.mockMvc.perform(put(Url + users.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(users.getId())))
                .andExpect(jsonPath("$.username", is(request.getUsername())))
                .andExpect(jsonPath("$.gender", is(users.getGender().name())));

        Users userFromDb = usersRepository.findById(Objects.requireNonNull(users.getId())).orElse(null);
        assertEquals(updatedUser, userFromDb);
    }

    @Test
    public void testUpdateUsers_whenIdIsNotExist_shouldReturnNotFoundException() throws Exception {
        UpdateUsersRequest request = generateUpdateUsersRequest();

        this.mockMvc.perform(put(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUsers_whenIdIsValidAndRequestIsInvalid_shouldReturnBadRequest() throws Exception {
        Users users = usersRepository.save(generateUsers());
        UpdateUsersRequest request = new UpdateUsersRequest("", Gender.MALE);

        this.mockMvc.perform(put(Url + users.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
