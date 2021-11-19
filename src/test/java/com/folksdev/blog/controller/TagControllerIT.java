package com.folksdev.blog.controller;

import com.folksdev.blog.IntegrationTestSupport;
import com.folksdev.blog.dto.request.CreateTagRequest;
import com.folksdev.blog.dto.request.UpdateTagRequest;
import com.folksdev.blog.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TagControllerIT extends IntegrationTestSupport {

    public String Url = "/v1/tag/";

    @Test
    public void testGetAllTag_whenRequestIsValid_shouldReturnEmptyList() throws Exception{
        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Tag> tagList = tagRepository.findAll();
        assertEquals(0, tagList.size());
    }

    @Test
    public void testGetAllTag_whenRequestIsValid_shouldReturnListOfTagDto() throws Exception{
        Tag tag = generateTag();
        tagRepository.save(tag);

        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Tag> tagList = tagRepository.findAll();
        assertEquals(1, tagList.size());
    }

    @Test
    public void testGetTagById_whenTagIdNotExist_shouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTagById_whenTagIdIsExist_shouldReturnTagDto() throws Exception{
        Tag tag = tagRepository.save(generateTag());

        this.mockMvc.perform(get(Url + tag.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(tag.getId())))
                .andExpect(jsonPath("$.name", is(tag.getName())));
    }

    @Test
    public void testCreateTag_whenCreateTagRequestIsInvalid_shouldNotCreateTagAndReturnBadRequest() throws Exception{
        CreateTagRequest tagRequest = new CreateTagRequest("");

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(tagRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", notNullValue()));

        List<Tag> tagList = tagRepository.findAll();

        assertEquals(0, tagList.size());
    }

    @Test
    public void testCreateTag_whenCreateTagRequestIsValid_shouldCreateTagAndReturnTagDto() throws Exception{
        CreateTagRequest tagRequest = generateCreateTagRequest();

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(tagRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("name")));

        List<Tag> tagList = tagRepository.findAll();

        assertEquals(1, tagList.size());
    }

    @Test
    public void testDeleteTagById_whenIdIsExist_shouldDeleteTagAndReturnVoid() throws Exception {
        Tag tag = tagRepository.save(generateTag());

        this.mockMvc.perform(delete(Url + tag.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteTagById_whenIdNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(delete(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUpdateTag_whenIdAndRequestIsValid_shouldUpdateTagAndReturnTagDto() throws Exception {
        Tag tag = tagRepository.save(generateTag());
        UpdateTagRequest request = generateUpdateTagRequest();
        Tag updatedTag = generateUpdatedTag(tag, request);

        this.mockMvc.perform(put(Url + tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tag.getId())))
                .andExpect(jsonPath("$.name", is(request.getName())));

        Tag userFromDb = tagRepository.findById(Objects.requireNonNull(tag.getId())).get();
        assertEquals(updatedTag, userFromDb);
    }

    @Test
    public void testUpdateTag_whenIdIsNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(put(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateTag_whenIdIsValidAndRequestIsInvalid_shouldReturnBadRequest() throws Exception {
        Tag tag = tagRepository.save(generateTag());
        UpdateTagRequest request = new UpdateTagRequest("");

        this.mockMvc.perform(put(Url + tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
