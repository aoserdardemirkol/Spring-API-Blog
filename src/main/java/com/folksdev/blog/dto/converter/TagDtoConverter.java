package com.folksdev.blog.dto.converter;

import com.folksdev.blog.dto.TagDto;
import com.folksdev.blog.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TagDtoConverter extends BaseDtoConverter{

    public TagDto convert(Tag from) {
        return new TagDto(
                from.getId(),
                from.getName(),
                getEntryList(Objects.requireNonNull(from.getEntries())));
    }

    public List<TagDto> convertTagDtoList(List<Tag> tagList) {
        return tagList
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
