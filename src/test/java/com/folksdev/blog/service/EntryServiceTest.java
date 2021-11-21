package com.folksdev.blog.service;

import com.folksdev.blog.TestSupport;
import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.converter.EntryDtoConverter;
import com.folksdev.blog.dto.request.CreateEntryRequest;
import com.folksdev.blog.dto.request.UpdateEntryRequest;
import com.folksdev.blog.exception.EntryNotFoundException;
import com.folksdev.blog.exception.UsersNotFoundException;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Tag;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntryServiceTest extends TestSupport {
    private EntryRepository entryRepository;
    private UsersService usersService;
    private TagService tagService;
    private EntryDtoConverter entryDtoConverter;

    private EntryService entryService;

    @BeforeEach
    void setUp() {
        entryRepository = Mockito.mock(EntryRepository.class);
        usersService = Mockito.mock(UsersService.class);
        tagService = Mockito.mock(TagService.class);
        entryDtoConverter = Mockito.mock(EntryDtoConverter.class);

        entryService = new EntryService(entryRepository, usersService, tagService, entryDtoConverter);
    }

    @Test
    void testGetAllEntry_shouldReturnListOfEntryDto(){
        List<Entry> entryList = generateEntryList();
        List<EntryDto> entryDtoList = generateEntryDtoList();

        Mockito.when(entryRepository.findAll()).thenReturn(entryList);
        Mockito.when(entryDtoConverter.convertEntryDtoList(entryList)).thenReturn(entryDtoList);

        List<EntryDto> result = entryService.getAllEntries();

        assertEquals(entryDtoList, result);

        Mockito.verify(entryRepository).findAll();
        Mockito.verify(entryDtoConverter).convertEntryDtoList(entryList);
    }

    @Test
    void testGetEntryById_whenEntryIdExist_shouldReturnEntryDto() {
        Entry entry = generateEntry();
        EntryDto entryDto = generateEntryDto();

        Mockito.when(entryRepository.findById("entryId")).thenReturn(Optional.of(entry));
        Mockito.when(entryDtoConverter.convert(entry)).thenReturn(entryDto);

        EntryDto result = entryService.getEntryById("entryId");

        assertEquals(entryDto, result);

        Mockito.verify(entryRepository).findById("entryId");
        Mockito.verify(entryDtoConverter).convert(entry);
    }

    @Test
    void testGetEntryById_whenEntryIdNotExist_shouldThrowEntryNotFoundException() {
        Mockito.when(entryRepository.findById("entryId")).thenThrow(EntryNotFoundException.class);

        assertThrows(EntryNotFoundException.class,
                () -> entryService.getEntryById("entryId"));

        Mockito.verify(entryRepository).findById("entryId");
        Mockito.verifyNoInteractions(entryDtoConverter);
    }

    @Test
    void testCreateEntry_whenUserIdNotExist_shouldThrowUsersNotFoundException() {
        CreateEntryRequest entryRequest = generateCreateEntryRequest();

        Mockito.when(usersService.findUsersById("authorId")).thenThrow(UsersNotFoundException.class);

        assertThrows(UsersNotFoundException.class,
                () -> entryService.createEntry(entryRequest));

        Mockito.verify(usersService).findUsersById(entryRequest.getAuthorId());
        Mockito.verifyNoInteractions(tagService);
        Mockito.verifyNoInteractions(entryRepository);
        Mockito.verifyNoInteractions(entryDtoConverter);
    }

    @Test
    void testCreateEntry_whenTagListNotExist_shouldReturnCreateEntryByDefaultTag() {
        Users users = generateUsers();
        Entry entry = generateEntry();
        EntryDto entryDto = generateEntryDto();
        List<String> idList = List.of("id1", "id2");
        List<Tag> tagList = List.of(
                new Tag("id1","tagName1"),
                new Tag("id2","tagName2")
        );

        CreateEntryRequest entryRequest = new CreateEntryRequest(
                "title",
                "content",
                idList,
                "authorId"
        );

        Mockito.when(usersService.findUsersById("authorId")).thenReturn(users);
        Mockito.when(tagService.getTagList(idList)).thenReturn(tagList);
        Mockito.when(entryRepository.save(entry)).thenReturn(entry);
        Mockito.when(entryDtoConverter.convert(entry)).thenReturn(entryDto);

        EntryDto result = entryService.createEntry(entryRequest);

        assertEquals(entryDto, result);

        Mockito.verify(usersService).findUsersById("authorId");
        Mockito.verify(tagService).getTagList(idList);
        Mockito.verify(entryRepository).save(entry);
        Mockito.verify(entryDtoConverter).convert(entry);
    }

    @Test
    void testCreateEntry_whenTagListExist_shouldReturnEntryDto() {
        Users users = generateUsers();
        Entry entry = generateEntry();
        EntryDto entryDto = generateEntryDto();

        Tag tag = generateTag();
        List<String> idList = List.of(Objects.requireNonNull(tag.getId()));
        List<Tag> tagList = List.of(tag);

        CreateEntryRequest entryRequest = new CreateEntryRequest(
                "title",
                "content",
                idList,
                "authorId"
        );

        Mockito.when(usersService.findUsersById("authorId")).thenReturn(users);
        Mockito.when(tagService.getTagList(idList)).thenReturn(tagList);
        Mockito.when(entryRepository.save(entry)).thenReturn(entry);
        Mockito.when(entryDtoConverter.convert(entry)).thenReturn(entryDto);

        EntryDto result = entryService.createEntry(entryRequest);

        assertEquals(entryDto, result);

        Mockito.verify(usersService).findUsersById("authorId");
        Mockito.verify(tagService).getTagList(idList);
        Mockito.verify(entryRepository).save(entry);
        Mockito.verify(entryDtoConverter).convert(entry);
    }

    @Test
    void testUpdateEntry_whenEntryIdNotExist_shouldThrowEntryNotFoundException(){
        UpdateEntryRequest entryRequest = generateUpdateEntryRequest();

        Mockito.when(entryRepository.findById("entryId")).thenThrow(EntryNotFoundException.class);

        assertThrows(EntryNotFoundException.class,
                () -> entryService.updateEntry("entryId", entryRequest));

        Mockito.verify(entryRepository).findById("entryId");
        Mockito.verifyNoInteractions(entryDtoConverter);
    }

    @Test
    void testUpdateEntry_whenTagListExist_shouldReturnEntryDto() {
        Tag tag = generateTag();
        List<String> idList = List.of(Objects.requireNonNull(tag.getId()));
        UpdateEntryRequest updateEntryRequest = new UpdateEntryRequest(
                "title",
                "content",
                idList
        );
        Entry entry = generateEntry();
        EntryDto entryDto = generateEntryDto();

        Mockito.when(entryRepository.findById("entryId")).thenReturn(Optional.ofNullable(entry));
        Mockito.when(tagService.getTagList(idList))
                .thenReturn(List.of(
                        new Tag("tagId1", "name1"),
                        new Tag("tagId2", "tech")));
        assert entry != null;
        Mockito.when(entryRepository.save(entry)).thenReturn(entry);
        Mockito.when(entryDtoConverter.convert(entry)).thenReturn(entryDto);

        EntryDto result = entryService.updateEntry("entryId", updateEntryRequest);

        assertEquals(entryDto, result);

        Mockito.verify(entryRepository).findById("entryId");
        Mockito.verify(tagService).getTagList(idList);
        Mockito.verify(entryRepository).save(entry);
        Mockito.verify(entryDtoConverter).convert(entry);
    }

    @Test
    void testUpdateEntry_whenTagListNotExist_shouldReturnUpdateEntryByDefaultTag() {
        List<String> idList = List.of("id1", "id2");
        UpdateEntryRequest updateEntryRequest = new UpdateEntryRequest(
                "title",
                "content",
                idList
        );
        Entry entry = generateEntry();
        EntryDto entryDto = generateEntryDto();

        Mockito.when(entryRepository.findById("entryId")).thenReturn(Optional.ofNullable(entry));
        Mockito.when(tagService.getTagList(idList))
                .thenReturn(List.of(
                        new Tag("tagId1", "name1"),
                        new Tag("tagId2", "tech")));
        assert entry != null;
        Mockito.when(entryRepository.save(entry)).thenReturn(entry);
        Mockito.when(entryDtoConverter.convert(entry)).thenReturn(entryDto);

        EntryDto result = entryService.updateEntry("entryId", updateEntryRequest);

        assertEquals(entryDto, result);

        Mockito.verify(entryRepository).findById("entryId");
        Mockito.verify(tagService).getTagList(idList);
        Mockito.verify(entryRepository).save(entry);
        Mockito.verify(entryDtoConverter).convert(entry);
    }

    @Test
    void testDeleteEntryById_whenEntryIdExist_shouldReturnVoid(){
        Entry entry = generateEntry();

        Mockito.when(entryRepository.findById("entryId")).thenReturn(Optional.ofNullable(entry));

        entryService.deleteEntryById("entryId");

        Mockito.verify(entryRepository).deleteById("entryId");
    }

    @Test
    void testDeleteEntryById_whenEntryIdNotExist_shouldThrowEntryNotFoundException(){
        Mockito.when(entryRepository.findById("entryId")).thenThrow(EntryNotFoundException.class);

        assertThrows(EntryNotFoundException.class, () -> entryService.deleteEntryById("entryId"));

        Mockito.verify(entryRepository).findById("entryId");
    }
}
