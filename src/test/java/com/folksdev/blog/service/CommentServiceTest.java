package com.folksdev.blog.service;

import com.folksdev.blog.TestSupport;
import com.folksdev.blog.dto.CommentDto;
import com.folksdev.blog.dto.converter.CommentDtoConverter;
import com.folksdev.blog.dto.request.CreateCommentRequest;
import com.folksdev.blog.dto.request.UpdateCommentRequest;
import com.folksdev.blog.exception.CommentNotFoundException;
import com.folksdev.blog.exception.EntryNotFoundException;
import com.folksdev.blog.exception.UsersNotFoundException;
import com.folksdev.blog.model.Comment;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentServiceTest extends TestSupport {

    private UsersService usersService;
    private EntryService entryService;
    private CommentRepository commentRepository;
    private CommentDtoConverter commentDtoConverter;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = Mockito.mock(CommentRepository.class);
        usersService = Mockito.mock(UsersService.class);
        entryService = Mockito.mock(EntryService.class);
        commentDtoConverter = Mockito.mock(CommentDtoConverter.class);

        commentService = new CommentService(usersService, entryService, commentRepository, commentDtoConverter);
    }

    @Test
    void testGetAllComment_shouldReturnListOfCommentDto(){
        List<Comment> commentList = generateCommentList();
        List<CommentDto> commentDtoList = generateCommentDtoList();

        Mockito.when(commentRepository.findAll()).thenReturn(commentList);
        Mockito.when(commentDtoConverter.convertCommentDtoList(commentList)).thenReturn(commentDtoList);

        List<CommentDto> result = commentService.getAllComments();

        assertEquals(commentDtoList, result);

        Mockito.verify(commentRepository).findAll();
        Mockito.verify(commentDtoConverter).convertCommentDtoList(commentList);
    }

    @Test
    void testGetCommentById_whenCommentIdExist_shouldReturnCommentDto() {
        Comment comment = generateComment();
        CommentDto commentDto = generateCommentDto();

        Mockito.when(commentRepository.findById("commentId")).thenReturn(Optional.of(comment));
        Mockito.when(commentDtoConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById("commentId");

        assertEquals(commentDto, result);

        Mockito.verify(commentRepository).findById("commentId");
        Mockito.verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testGetCommentById_whenCommentIdNotExist_shouldThrowCommentNotFoundException() {
        Mockito.when(commentRepository.findById("commentId")).thenThrow(CommentNotFoundException.class);

        assertThrows(CommentNotFoundException.class,
                () -> commentService.getCommentById("commentId"));

        Mockito.verify(commentRepository).findById("commentId");
        Mockito.verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testCreateComment_whenUserIdNotExist_shouldThrowUsersNotFoundException() {
        CreateCommentRequest commentRequest = generateCreateCommentRequest();

        Mockito.when(usersService.findUsersById("authorId")).thenThrow(UsersNotFoundException.class);

        assertThrows(UsersNotFoundException.class,
                () -> commentService.createComment(commentRequest));

        Mockito.verify(usersService).findUsersById("authorId");
        Mockito.verifyNoInteractions(entryService);
        Mockito.verifyNoInteractions(commentRepository);
        Mockito.verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testCreateComment_whenUserIdExistAndEntryIdNotExist_shouldThrowEntryNotFoundException() {
        CreateCommentRequest commentRequest = generateCreateCommentRequest();

        Mockito.when(usersService.findUsersById("authorId")).thenReturn(generateUsers());
        Mockito.when(entryService.findEntryById("entryId")).thenThrow(EntryNotFoundException.class);

        assertThrows(EntryNotFoundException.class,
                () -> commentService.createComment(commentRequest));

        Mockito.verify(usersService).findUsersById("authorId");
        Mockito.verify(entryService).findEntryById("entryId");
        Mockito.verifyNoInteractions(commentRepository);
        Mockito.verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testCreateComment_whenUserIdExistAndEntryExist_shouldReturnCommentDto() {
        CreateCommentRequest commentRequest = generateCreateCommentRequest();

        Comment comment = generateComment();
        Users users = generateUsers();
        Entry entry = generateEntry();

        CommentDto commentDto = generateCommentDto();

        Mockito.when(usersService.findUsersById("authorId")).thenReturn(users);
        Mockito.when(entryService.findEntryById("entryId")).thenReturn(entry);
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(commentDtoConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(commentRequest);

        assertEquals(commentDto, result);

        Mockito.verify(usersService).findUsersById("authorId");
        Mockito.verify(entryService).findEntryById("entryId");
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testUpdateComment_whenCommentIdNotExist_shouldThrowCommentNotFoundException(){
        UpdateCommentRequest commentRequest = generateUpdateCommentRequest();

        Mockito.when(commentRepository.findById("commentId")).thenThrow(CommentNotFoundException.class);

        assertThrows(CommentNotFoundException.class,
                () -> commentService.updateComment("commentId", commentRequest));

        Mockito.verify(commentRepository).findById("commentId");
        Mockito.verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testUpdateComment_whenCommentExist_shouldReturnCommentDto(){
        UpdateCommentRequest commentRequest = generateUpdateCommentRequest();
        Comment comment = generateComment();
        CommentDto commentDto = generateCommentDto();

        Mockito.when(commentRepository.findById("commentId")).thenReturn(Optional.ofNullable(comment));
        assert comment != null;
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(commentDtoConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment("commentId", commentRequest);

        assertEquals(commentDto, result);

        Mockito.verify(commentRepository).findById("commentId");
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testDeleteCommentById_whenCommentIdExist_shouldReturnVoid(){
        Comment comment = generateComment();

        Mockito.when(commentRepository.findById("commentId")).thenReturn(Optional.ofNullable(comment));

        commentService.deleteCommentById("commentId");

        Mockito.verify(commentRepository).deleteById("commentId");
    }

    @Test
    void testDeleteCommentById_whenCommentIdNotExist_shouldThrowCommentNotFoundException(){
        Mockito.when(commentRepository.findById("commentId")).thenThrow(CommentNotFoundException.class);

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteCommentById("commentId"));

        Mockito.verify(commentRepository).findById("commentId");
    }
}
