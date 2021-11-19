package com.folksdev.blog.service;

import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.converter.EntryDtoConverter;
import com.folksdev.blog.dto.request.CreateEntryRequest;
import com.folksdev.blog.dto.request.UpdateEntryRequest;
import com.folksdev.blog.exception.EntryNotFoundException;
import com.folksdev.blog.model.Entry;
import com.folksdev.blog.model.Tag;
import com.folksdev.blog.model.Users;
import com.folksdev.blog.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {
    private final UsersService usersService;
    private final EntryRepository entryRepository;
    private final TagService tagService;
    private final EntryDtoConverter entryDtoConverter;

    public EntryService(EntryRepository entryRepository,
                        UsersService usersService,
                        TagService tagService,
                        EntryDtoConverter entryDtoConverter){
        this.entryRepository = entryRepository;
        this.usersService = usersService;
        this.tagService = tagService;
        this.entryDtoConverter = entryDtoConverter;
    }

    public List<EntryDto> getAllEntries() {
        return entryDtoConverter.convertEntryDtoList(entryRepository.findAll());
    }

    public EntryDto getEntryById(String id){
        return entryDtoConverter.convert(findEntryById(id));
    }

    public EntryDto createEntry(CreateEntryRequest entryRequest) {
        Users users = usersService.findUsersById(entryRequest.getAuthorId());
        List<Tag> tagList = new ArrayList<>(tagService.getTagList(entryRequest.getTagIds()));

        Entry entry = new Entry(
                entryRequest.getTitle(),
                entryRequest.getContent(),
                null,
                tagList,
                users
        );

        return entryDtoConverter.convert(entryRepository.save(entry));
    }

    public EntryDto updateEntry(String id, UpdateEntryRequest entryRequest){
        Entry entry = findEntryById(id);
        List<Tag> tagList = new ArrayList<>(tagService.getTagList(entryRequest.getTagIds()));

        Entry updatedEntry = new Entry(
                entry.getId(),
                entryRequest.getTitle(),
                entryRequest.getContent(),
                entry.getCreateDate(),
                LocalDateTime.now(),
                tagList,
                entry.getUsers()
        );

        return entryDtoConverter.convert(entryRepository.save(updatedEntry));
    }

    public void deleteEntryById(String id){
        findEntryById(id);
        entryRepository.deleteById(id);
    }

    protected Entry findEntryById(String id) {
        return entryRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Entry could not find by id:" + id));
    }
}
