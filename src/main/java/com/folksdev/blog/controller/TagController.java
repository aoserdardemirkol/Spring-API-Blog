package com.folksdev.blog.controller;

import com.folksdev.blog.dto.TagDto;
import com.folksdev.blog.dto.request.CreateTagRequest;
import com.folksdev.blog.dto.request.UpdateTagRequest;
import com.folksdev.blog.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/v1/tag")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable String id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody CreateTagRequest createTagRequest){
        return new ResponseEntity<>(tagService.createTag(createTagRequest), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable String id, @Valid @RequestBody UpdateTagRequest tagRequest){
        return ResponseEntity.ok(tagService.updateTag(id, tagRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable(name = "id") String id){
        tagService.deleteTagById(id);
        return ResponseEntity.ok().build();
    }
}
