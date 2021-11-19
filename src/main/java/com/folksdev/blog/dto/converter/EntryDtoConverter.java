package com.folksdev.blog.dto.converter;

import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.UsersDto;
import com.folksdev.blog.model.Entry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class EntryDtoConverter extends BaseDtoConverter{

    public EntryDto convert(Entry from) {
        return new EntryDto(
                from.getId(),
                from.getTitle(),
                from.getContent(),
                from.getCreateDate(),
                from.getUpdateDate(),
                getTagList(from.getTags()),
                new UsersDto(from.getUsers().getId(),
                        from.getUsers().getUsername(),
                        from.getUsers().getGender()),
                getCommentList(Objects.requireNonNull(from.getComments())));
    }

    public List<EntryDto> convertEntryDtoList(List<Entry> entryList) {
        return entryList
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}