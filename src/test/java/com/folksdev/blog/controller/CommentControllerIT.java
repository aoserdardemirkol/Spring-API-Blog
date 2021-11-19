package com.folksdev.blog.controller;

import com.folksdev.blog.IntegrationTestSupport;
import com.folksdev.blog.dto.request.CreateCommentRequest;
import com.folksdev.blog.dto.request.UpdateCommentRequest;
import com.folksdev.blog.model.Comment;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentControllerIT extends IntegrationTestSupport {
    public String Url = "/v1/comment/";

    @Test
    public void testGetAllComment_whenRequestIsValid_shouldReturnEmptyList() throws Exception{
        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Comment> entryList = commentRepository.findAll();
        assertEquals(0, entryList.size());
    }

    @Test
    public void testGetAllComment_whenRequestIsValid_shouldReturnListOfCommentDto() throws Exception{
        generateCommentToIT();

        this.mockMvc.perform(get(Url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Comment> commentList = commentRepository.findAll();
        assertEquals(1, commentList.size());
    }

    @Test
    public void testGetCommentById_whenCommentIdNotExist_shouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommentById_whenCommentIdIsExist_shouldReturnCommentDto() throws Exception{
        Comment comment = generateCommentToIT();

        this.mockMvc.perform(get(Url + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(comment.getId())))
                .andExpect(jsonPath("$.content", is(comment.getContent())));
    }

    @Test
    public void testCreateComment_whenCreateCommentRequestIsInvalid_shouldNotCreateCommentAndReturnBadRequest() throws Exception{
        CreateCommentRequest commentRequest = generateCreateCommentBadRequest();

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(commentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", notNullValue()));

        List<Comment> commentList = commentRepository.findAll();

        assertEquals(0, commentList.size());
    }

    @Test
    public void testCreateComment_whenCreateCommentRequestIsValid_shouldCreateCommentAndReturnCommentDto() throws Exception{
        Users users = usersRepository.save(generateUsers());
        Entry entry = entryRepository.save(new Entry(
                "entryId",
                "title",
                "content",
                LocalDateTime.of(2021, 11, 14, 20, 30),
                null,
                Collections.emptyList(),
                users,
                Collections.emptyList())
        );
        CreateCommentRequest commentRequest = new CreateCommentRequest(
                "content",
                Objects.requireNonNull(users.getId()),
                Objects.requireNonNull(entry.getId())
        );

        this.mockMvc.perform(post(Url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(commentRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is("content")));

        List<Comment> commentList = commentRepository.findAll();

        assertEquals(1, commentList.size());
    }

    @Test
    public void testDeleteCommentById_whenIdIsExist_shouldDeleteCommentAndReturnVoid() throws Exception {
        Comment comment =  generateCommentToIT();

        this.mockMvc.perform(delete(Url + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteCommentById_whenIdNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(delete(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateComment_whenIdAndRequestIsValid_shouldUpdateCommentAndReturnCommentDto() throws Exception {
        Comment comment =  generateCommentToIT();

        UpdateCommentRequest request = generateUpdateCommentRequest();

        Comment updatedComment = new Comment(
                comment.getId(),
                request.getContent(),
                comment.getCreateDate(),
                comment.getUpdateDate(),
                comment.getEntry(),
                comment.getUsers()
        );

        this.mockMvc.perform(put(Url + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(comment.getId())))
                .andExpect(jsonPath("$.content", is(request.getContent())));

        Comment commentFromDb = commentRepository.findById(Objects.requireNonNull(comment.getId())).get();
        assertEquals(updatedComment, commentFromDb);
    }

    @Test
    public void testUpdateComment_whenIdIsNotExist_shouldReturnNotFoundException() throws Exception {
        this.mockMvc.perform(put(Url + "not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateComment_whenIdIsValidAndRequestIsInvalid_shouldReturnBadRequest() throws Exception {
        Comment comment = generateCommentToIT();

        UpdateCommentRequest request = generateUpdateCommentBadRequest();

        this.mockMvc.perform(put(Url + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
