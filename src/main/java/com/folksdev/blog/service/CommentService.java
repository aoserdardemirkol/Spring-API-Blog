package com.folksdev.blog.service;

import com.folksdev.blog.dto.CommentDto;
import com.folksdev.blog.dto.converter.CommentDtoConverter;
import com.folksdev.blog.dto.request.CreateCommentRequest;
import com.folksdev.blog.dto.request.UpdateCommentRequest;
import com.folksdev.blog.exception.CommentNotFoundException;
import com.folksdev.blog.model.Comment;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final UsersService usersService;
    private final EntryService entryService;
    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;

    public CommentService(UsersService usersService,
                          EntryService entryService,
                          CommentRepository commentRepository,
                          CommentDtoConverter commentDtoConverter){
        this.usersService = usersService;
        this.entryService = entryService;
        this.commentRepository = commentRepository;
        this.commentDtoConverter = commentDtoConverter;
    }

    public List<CommentDto> getAllComments() {
        return commentDtoConverter.convertCommentDtoList(commentRepository.findAll());
    }

    public CommentDto getCommentById(String id){
        return commentDtoConverter.convert(findCommentById(id));
    }

    public CommentDto createComment(CreateCommentRequest commentRequest) {
        Users users = usersService.findUsersById(commentRequest.getAuthorId());
        Entry entry = entryService.findEntryById(commentRequest.getEntryId());

        Comment comment = new Comment(
                commentRequest.getContent(),
                null,
                entry,
                users
        );

        return commentDtoConverter.convert(commentRepository.save(comment));
    }

    public CommentDto updateComment(String id, UpdateCommentRequest updateCommentRequest) {

        Comment comment = findCommentById(id);

        Comment updatedComment = new Comment(
                comment.getId(),
                updateCommentRequest.getContent(),
                comment.getCreateDate(),
                LocalDateTime.now(),
                comment.getEntry(),
                comment.getUsers()
        );

        return commentDtoConverter.convert(commentRepository.save(updatedComment));
    }

    public void deleteCommentById(String id){
        findCommentById(id);
        commentRepository.deleteById(id);
    }

    protected Comment findCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment could not find by id:" + id));
    }
}
