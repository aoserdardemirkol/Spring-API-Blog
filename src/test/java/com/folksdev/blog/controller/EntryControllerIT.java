package com.folksdev.blog.controller;

import com.folksdev.blog.IntegrationTestSupport;
import com.folksdev.blog.dto.request.CreateEntryRequest;
import com.folksdev.blog.dto.request.UpdateEntryRequest;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Tag;
import com.folksdev.blog.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EntryControllerIT extends IntegrationTestSupport {

    public String Url = "/v1/entry/";

    @Test
    public void testGetAllEntry_whenRequestIsValid_shouldReturnEmptyList() throws Exception{
        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Entry> entryList = entryRepository.findAll();
        assertEquals(0, entryList.size());
    }

    @Test
    public void testGetAllEntry_whenRequestIsValid_shouldReturnListOfEntryDto() throws Exception{
        generateEntryToIT();

        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Entry> entryList = entryRepository.findAll();
        assertEquals(1, entryList.size());
    }

    @Test
    public void testGetEntryById_whenEntryIdNotExist_shouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEntryById_whenEntryIdIsExist_shouldReturnEntryDto() throws Exception{
        Entry entry = generateEntryToIT();

        this.mockMvc.perform(get(Url + entry.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(entry.getId())))
                .andExpect(jsonPath("$.title", is(entry.getTitle())))
                .andExpect(jsonPath("$.content", is(entry.getContent())))
                .andExpect(jsonPath("$.users", notNullValue()));
    }

    @Test
    public void testCreateEntry_whenCreateEntryRequestIsInvalid_shouldNotCreateEntryAndReturnBadRequest() throws Exception{
        CreateEntryRequest entryRequest = generateCreateEntryBadRequest();

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(entryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()));

        List<Entry> entryList = entryRepository.findAll();

        assertEquals(0, entryList.size());
    }

    @Test
    public void testCreateEntry_whenCreateEntryRequestIsValid_shouldCreateEntryAndReturnEntryDto() throws Exception{
        Users users = usersRepository.save(generateUsers());
        List<Tag> tagList = tagRepository.saveAll(generateTagList());
        CreateEntryRequest entryRequest = new CreateEntryRequest(
                "title",
                "content",
                tagList.stream().map(Tag::getId).collect(Collectors.toList()),
                Objects.requireNonNull(users.getId()));

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(entryRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.content", is("content")))
                .andExpect(jsonPath("$.tags", is(hasSize(1))));

        List<Entry> entryList = entryRepository.findAll();

        assertEquals(1, entryList.size());
    }

    @Test
    public void testDeleteEntryById_whenIdIsExist_shouldDeleteEntryAndReturnVoid() throws Exception {
        Entry entry = generateEntryToIT();

        this.mockMvc.perform(delete(Url + entry.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteEntryById_whenIdNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(delete(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUpdateEntry_whenIdAndRequestIsValid_shouldUpdateEntryAndReturnEntryDto() throws Exception {
        Entry entry = generateEntryToIT();

        UpdateEntryRequest request = new UpdateEntryRequest(
                "title",
                "content",
                List.of("tagIds"));

        List<Tag> tagList = tagRepository.findAllByIdIn(request.getTagIds());

        Entry updatedEntry = new Entry(
                entry.getId(),
                request.getTitle(),
                request.getContent(),
                entry.getCreateDate(),
                entry.getUpdateDate(),
                tagList,
                entry.getUsers(),
                entry.getComments()
        );

        this.mockMvc.perform(put(Url + entry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(entry.getId())))
                .andExpect(jsonPath("$.title", is(request.getTitle())))
                .andExpect(jsonPath("$.content", is(request.getContent())))
                .andExpect(jsonPath("$.users", notNullValue()));

        Entry entryFromDb = entryRepository.findById(Objects.requireNonNull(entry.getId())).get();
        assertEquals(updatedEntry, entryFromDb);
    }

    @Test
    public void testUpdateEntry_whenIdIsNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(put(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEntry_whenIdIsValidAndRequestIsInvalid_shouldReturnBadRequest() throws Exception {
        Entry entry = generateEntryToIT();

        UpdateEntryRequest request = generateUpdateEntryBadRequest();

        this.mockMvc.perform(put(Url + entry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
