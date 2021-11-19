package com.folksdev.blog.service;

import com.folksdev.blog.TestSupport;
import com.folksdev.blog.dto.TagDto;
import com.folksdev.blog.dto.converter.TagDtoConverter;
import com.folksdev.blog.dto.request.CreateTagRequest;
import com.folksdev.blog.dto.request.UpdateTagRequest;
import com.folksdev.blog.exception.TagNotFoundException;
import com.folksdev.blog.model.Tag;
import com.folksdev.blog.repository.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagServiceTest extends TestSupport {

    private TagRepository tagRepository;
    private TagDtoConverter tagDtoConverter;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagRepository = Mockito.mock(TagRepository.class);
        tagDtoConverter = Mockito.mock(TagDtoConverter.class);

        tagService = new TagService(tagRepository,tagDtoConverter);
    }

    @Test
    void testGetAllTag_shouldReturnListOfTagDto(){
        List<Tag> tagList = generateTagList();
        List<TagDto> tagDtoList = generateTagDtoList();

        Mockito.when(tagRepository.findAll()).thenReturn(tagList);
        Mockito.when(tagDtoConverter.convertTagDtoList(tagList)).thenReturn(tagDtoList);

        List<TagDto> result = tagService.getAllTags();

        assertEquals(tagDtoList, result);

        Mockito.verify(tagRepository).findAll();
        Mockito.verify(tagDtoConverter).convertTagDtoList(tagList);
    }

    @Test
    void testGetTagById_whenTagIdExist_shouldReturnTagDto() {
        Tag tag = generateTag();
        TagDto tagDto = generateTagDto();

        Mockito.when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));
        Mockito.when(tagDtoConverter.convert(tag)).thenReturn(tagDto);

        TagDto result = tagService.getTagById("tagId");

        assertEquals(tagDto, result);

        Mockito.verify(tagRepository).findById("tagId");
        Mockito.verify(tagDtoConverter).convert(tag);
    }

    @Test
    void testGetTagById_whenTagIdNotExist_shouldThrowTagNotFoundException() {
        Mockito.when(tagRepository.findById("tagId")).thenThrow(TagNotFoundException.class);

        assertThrows(TagNotFoundException.class,
                () -> tagService.getTagById("tagId"));

        Mockito.verify(tagRepository).findById("tagId");
        Mockito.verifyNoInteractions(tagDtoConverter);
    }

    @Test
    void testCreateTag_whenCreateTagRequest_shouldReturnTagDto(){
        CreateTagRequest tagRequest = generateCreateTagRequest();

        Tag tag = generateTag();
        TagDto tagDto = generateTagDto();

        Mockito.when(tagDtoConverter.convert(tagRepository.save(tag))).thenReturn(tagDto);

        TagDto result = tagService.createTag(tagRequest);

        assertEquals(tagDto, result);

        Mockito.verify(tagDtoConverter).convert(tagRepository.save(tag));
    }

    @Test
    void testUpdateTag_whenTagIdNotExist_shouldThrowTagNotFoundException(){
        UpdateTagRequest tagRequest = generateUpdateTagRequest();

        Mockito.when(tagRepository.findById("tagId")).thenThrow(TagNotFoundException.class);

        assertThrows(TagNotFoundException.class, () -> tagService.updateTag("tagId", tagRequest));

        Mockito.verify(tagRepository).findById("tagId");
        Mockito.verifyNoInteractions(tagDtoConverter);
    }

    @Test
    void testUpdateTag_whenUpdateRequest_shouldReturnTagDto(){
        UpdateTagRequest tagRequest = generateUpdateTagRequest();
        Tag tag = generateTag();
        TagDto tagDto = generateTagDto();

        Mockito.when(tagRepository.findById("tagId")).thenReturn(Optional.ofNullable(tag));
        assert tag != null;
        Mockito.when(tagRepository.save(tag)).thenReturn(tag);
        Mockito.when(tagDtoConverter.convert(tag)).thenReturn(tagDto);

        TagDto result = tagService.updateTag("tagId", tagRequest);

        assertEquals(tagDto, result);

        Mockito.verify(tagRepository).findById("tagId");
        Mockito.verify(tagRepository).save(tag);
        Mockito.verify(tagDtoConverter).convert(tag);
    }

    @Test
    void testDeleteTagById_whenUsersIdExist_shouldReturnVoid(){
        Tag tag = generateTag();
        TagDto tagDto = generateTagDto();

        Mockito.when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));
        Mockito.when(tagDtoConverter.convert(tag)).thenReturn(tagDto);

        tagService.deleteTagById("tagId");

        Mockito.verify(tagRepository).deleteById("tagId");
    }

    @Test
    void testDeleteTagById_whenTagIdNotExist_shouldThrowTagNotFoundException(){
        Mockito.when(tagRepository.findById("tagId")).thenThrow(TagNotFoundException.class);

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTagById("tagId"));

        Mockito.verify(tagRepository).findById("tagId");
        Mockito.verifyNoInteractions(tagDtoConverter);
    }

    @Test
    void testFindTagById_whenTagIdExist_shouldReturnTag(){
        Tag tag = generateTag();

        Mockito.when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));

        Tag result = tagService.findTagById("tagId");

        assertEquals(tag, result);

        Mockito.verify(tagRepository).findById("tagId");
    }

    @Test
    void testFindTagById_whenTagIdNotExist_shouldThrowTagNotFoundException(){
        Mockito.when(tagRepository.findById("tagId")).thenThrow(TagNotFoundException.class);

        assertThrows(TagNotFoundException.class, () -> tagService.findTagById("tagId"));

        Mockito.verify(tagRepository).findById("tagId");
    }

    @Test
    public void testGetTagList_whenIdListExist_shouldReturnTagList() {
        List<Tag> tagList = List.of(
                new Tag("id1","tagName1", Collections.emptyList()),
                new Tag("id2","tagName2", Collections.emptyList())
        );
        List<String> idList = List.of("id1", "id2");

        Mockito.when(tagRepository.findAllByIdIn(idList)).thenReturn(tagList);

        List<Tag> result = tagService.getTagList(idList);

        Assertions.assertEquals(tagList, result);

        Mockito.verify(tagRepository).findAllByIdIn(idList);
    }

    @Test
    public void testGetTagList_whenIdListNotExist_shouldReturnDefaultTagList() {
        List<Tag> tagList = List.of(
                new Tag("name")
        );
        List<String> idList = List.of("id1", "id2");

        Mockito.when(tagRepository.findAllByIdIn(idList)).thenReturn(Collections.emptyList());

        List<Tag> result = tagService.getTagList(idList);

        Assertions.assertEquals(tagList, result);

        Mockito.verify(tagRepository).findAllByIdIn(idList);
    }
}
