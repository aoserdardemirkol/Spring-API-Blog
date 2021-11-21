package com.folksdev.blog;

import com.folksdev.blog.dto.CommentDto;
import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.TagDto;
import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.dto.request.*;
import com.folksdev.blog.model.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TestSupport {
    public CreateUsersRequest generateCreateUsersRequest(){
        return new CreateUsersRequest(
                "username",
                Gender.MALE
        );
    }

    public UpdateUsersRequest generateUpdateUsersRequest(){
        return new UpdateUsersRequest(
                "username",
                Gender.MALE
        );
    }

    public Users generateUpdatedUser(Users from, UpdateUsersRequest request) {
        return new Users(
                from.getId(),
                request.getUsername(),
                request.getGender()
        );
    }

    public Users generateUsers(){
        return new Users(
                "username",
                Gender.MALE);
    }

    public UsersDto generateUsersDto(){
        return new UsersDto(
                "userId",
                "username",
                Gender.MALE,
                Collections.emptyList(),
                Collections.emptyList());
    }

    public List<Users> generateUsersList(){
        Users users = generateUsers();
        return List.of(users);
    }

    public List<UsersDto> generateUsersDtoList(){
        UsersDto users = generateUsersDto();
        return List.of(users);
    }
    
    public CreateTagRequest generateCreateTagRequest(){
        return new CreateTagRequest(
                "name"
        );
    }

    public UpdateTagRequest generateUpdateTagRequest(){
        return new UpdateTagRequest(
                "name"
        );
    }

    public Tag generateUpdatedTag(Tag from, UpdateTagRequest request) {
        return new Tag(
                from.getId(),
                request.getName()
        );
    }

    public Tag generateTag(){
        return new Tag(
                "name"
        );
    }

    public TagDto generateTagDto(){
        return new TagDto(
                "tagId",
                "name",
                Collections.emptyList()
        );
    }

    public List<Tag> generateTagList(){
        Tag tag = generateTag();
        return List.of(tag);
    }

    public List<TagDto> generateTagDtoList(){
        TagDto tagDto = generateTagDto();
        return List.of(tagDto);
    }

    public CreateEntryRequest generateCreateEntryRequest(){
        return new CreateEntryRequest(
                "title",
                "content",
                Collections.emptyList(),
                "authorId"
        );
    }

    public CreateEntryRequest generateCreateEntryBadRequest(){
        return new CreateEntryRequest(
                "",
                "",
                Collections.emptyList(),
                "authorId"
        );
    }

    public UpdateEntryRequest generateUpdateEntryRequest(){
        return new UpdateEntryRequest(
                "title",
                "content",
                Collections.emptyList()
        );
    }

    public UpdateEntryRequest generateUpdateEntryBadRequest(){
        return new UpdateEntryRequest(
                "",
                "",
                Collections.emptyList()
        );
    }

    public Entry generateEntry(){
        return new Entry(
                "title",
                "content",
                LocalDateTime.of(2021, 11, 14, 20, 30),
                Collections.emptyList(),
                generateUsers()
        );
    }

    public EntryDto generateEntryDto(){
        return new EntryDto(
                "entryId",
                "title",
                "content",
                LocalDateTime.of(2021, 11, 14, 20, 30),
                null,
                Collections.emptyList(),
                generateUsersDto(),
                Collections.emptyList()
        );
    }

    public List<Entry> generateEntryList(){
        Entry entry = generateEntry();
        return List.of(entry);
    }

    public List<EntryDto> generateEntryDtoList(){
        EntryDto entryDto = generateEntryDto();
        return List.of(entryDto);
    }

    public CreateCommentRequest generateCreateCommentRequest(){
        return new CreateCommentRequest(
                "content",
                "authorId",
                "entryId");
    }

    public CreateCommentRequest generateCreateCommentBadRequest(){
        return new CreateCommentRequest(
                "",
                "",
                "");
    }

    public UpdateCommentRequest generateUpdateCommentRequest(){
        return new UpdateCommentRequest(
                "title"
        );
    }

    public UpdateCommentRequest generateUpdateCommentBadRequest(){
        return new UpdateCommentRequest(
                ""
        );
    }

    public Comment generateComment(){
        return new Comment(
                "content",
                LocalDateTime.of(2021, 11, 14, 20, 30),
                generateEntry(),
                generateUsers()
        );
    }

    public CommentDto generateCommentDto(){
        return new CommentDto(
                "commentId",
                "content",
                LocalDateTime.of(2021, 11, 14, 20, 30),
                LocalDateTime.of(2021, 11, 14, 20, 30),
                generateEntryDto(),
                generateUsersDto()
        );
    }

    public List<Comment> generateCommentList(){
        Comment comment = generateComment();
        return List.of(comment);
    }

    public List<CommentDto> generateCommentDtoList(){
        CommentDto commentDto = generateCommentDto();
        return List.of(commentDto);
    }
}
