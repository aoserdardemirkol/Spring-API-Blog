package com.folksdev.blog.dto.converter;

import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.model.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UsersDtoConverter extends BaseDtoConverter {

    public UsersDto convert(Users from) {
        return new UsersDto(
                from.getId(),
                from.getUsername(),
                from.getGender(),
                getEntryList(Objects.requireNonNull(from.getEntries())),
                getCommentList(Objects.requireNonNull(from.getComments())));
    }

    public List<UsersDto> convertUsersDtoList(List<Users> usersList) {
        return usersList
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
