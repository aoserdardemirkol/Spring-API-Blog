package com.folksdev.blog.controller;

import com.folksdev.blog.dto.EntryDto;
import com.folksdev.blog.dto.request.CreateEntryRequest;
import com.folksdev.blog.dto.request.UpdateEntryRequest;
import com.folksdev.blog.service.EntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/v1/entry")
public class EntryController {
    private final EntryService entryService;

    public EntryController(EntryService entryService){
        this.entryService = entryService;
    }

    @GetMapping
    public ResponseEntity<List<EntryDto>> getEntries() {
        return ResponseEntity.ok(entryService.getAllEntries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryDto> getEntryById(@PathVariable String id) {
        return ResponseEntity.ok(entryService.getEntryById(id));
    }

    @PostMapping
    public ResponseEntity<EntryDto> createEntry(@Valid @RequestBody CreateEntryRequest entryRequest){
        return new ResponseEntity<>(entryService.createEntry(entryRequest), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryDto> updateEntry(@PathVariable String id, @Valid @RequestBody UpdateEntryRequest entryRequest){
        return ResponseEntity.ok(entryService.updateEntry(id, entryRequest));
    }

    @DeleteMapping( "/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id){
        entryService.deleteEntryById(id);
        return ResponseEntity.ok().build();
    }
}
