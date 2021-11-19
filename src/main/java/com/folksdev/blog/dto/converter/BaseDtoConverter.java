package com.folksdev.blog.dto.converter;

import com.folksdev.blog.dto.CommentDto;
import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.TagDto;
import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.model.Comment;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BaseDtoConverter {

    protected List<CommentDto> getCommentList(List<Comment> comments) {
        return comments.stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getContent(),
                        c.getCreateDate(),
                        c.getUpdateDate(),
                        new EntryDto(c.getEntry().getId(),
                                c.getEntry().getTitle(),
                                c.getEntry().getContent(),
                                c.getEntry().getCreateDate(),
                                c.getEntry().getUpdateDate(),
                                getTagList(c.getEntry().getTags())),
                        new UsersDto(c.getUsers().getId(),
                                c.getUsers().getUsername(),
                                c.getUsers().getGender())))
                .collect(Collectors.toList());
    }


    protected List<TagDto> getTagList(List<Tag> tags) {
        return tags.stream()
                .map(t -> new TagDto(
                        t.getId(),
                        t.getName()))
                .collect(Collectors.toList());
    }

    protected List<EntryDto> getEntryList(List<Entry> entries) {
        return entries.stream()
                .map(e -> new EntryDto(
                        e.getId(),
                        e.getTitle(),
                        e.getContent(),
                        e.getCreateDate(),
                        e.getUpdateDate(),
                        getTagList(e.getTags())))
                .collect(Collectors.toList());
    }
}
