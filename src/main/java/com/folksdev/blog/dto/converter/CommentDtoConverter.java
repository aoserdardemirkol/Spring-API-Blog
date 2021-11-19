package com.folksdev.blog.dto.converter;

import com.folksdev.blog.dto.CommentDto;
import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentDtoConverter extends BaseDtoConverter{

    public CommentDto convert(Comment from) {
        return new CommentDto(
                from.getId(),
                from.getContent(),
                from.getCreateDate(),
                from.getUpdateDate(),
                new EntryDto(from.getEntry().getId(),
                        from.getEntry().getTitle(),
                        from.getEntry().getContent(),
                        from.getEntry().getCreateDate(),
                        from.getEntry().getUpdateDate(),
                        getTagList(from.getEntry().getTags())),
                new UsersDto(from.getUsers().getId(),
                        from.getUsers().getUsername(),
                        from.getUsers().getGender()));
    }

    public List<CommentDto> convertCommentDtoList(List<Comment> commentList) {
        return commentList
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
