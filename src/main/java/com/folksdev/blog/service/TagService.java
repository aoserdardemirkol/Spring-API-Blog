package com.folksdev.blog.service;

import com.folksdev.blog.dto.TagDto;
import com.folksdev.blog.dto.converter.TagDtoConverter;
import com.folksdev.blog.dto.request.CreateTagRequest;
import com.folksdev.blog.dto.request.UpdateTagRequest;
import com.folksdev.blog.exception.TagNotFoundException;
import com.folksdev.blog.model.Tag;
import com.folksdev.blog.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagDtoConverter tagDtoConverter;

    public TagService(TagRepository tagRepository,
                      TagDtoConverter tagDtoConverter) {
        this.tagRepository = tagRepository;
        this.tagDtoConverter = tagDtoConverter;
    }

    public List<TagDto> getAllTags() {
        return tagDtoConverter.convertTagDtoList(tagRepository.findAll());
    }

    public TagDto getTagById(String id){
        return tagDtoConverter.convert(findTagById(id));
    }

    public TagDto createTag(CreateTagRequest tagRequest) {
        Tag tag = new Tag(
                tagRequest.getName()
        );
        return tagDtoConverter.convert(tagRepository.save(tag));
    }

    public TagDto updateTag(String id, UpdateTagRequest updateTagRequest) {
        Tag tag = findTagById(id);

        Tag updatedTag = new Tag(
                tag.getId(),
                updateTagRequest.getName()
        );

        return tagDtoConverter.convert(tagRepository.save(updatedTag));
    }

    public void deleteTagById(String id){
        findTagById(id);
        tagRepository.deleteById(id);
    }

    protected Tag findTagById(String id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag could not find by id:" + id));
    }

    protected List<Tag> getTagList(List<String> idList){
        return Optional.of(tagRepository.findAllByIdIn(idList))
                .filter(a -> !a.isEmpty())
                .orElse(List.of(
                        new Tag("tagName")
                ));
    }
}
