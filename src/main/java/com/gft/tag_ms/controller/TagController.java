package com.gft.tag_ms.controller;

import com.gft.tag_ms.dto.TagExistsResponse;
import com.gft.tag_ms.dto.TagRequest;
import com.gft.tag_ms.dto.TagResponse;
import com.gft.tag_ms.dto.TagWithWordsResponse;
import com.gft.tag_ms.service.ITagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController implements TagControllerDoc {
    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }


    @Override
    @PostMapping
    public ResponseEntity<TagResponse> create(@RequestBody @Valid TagRequest request){

        TagResponse tagResponse = tagService.create(request);

        URI location =URI.create("/tags/" + tagResponse.id());

        return ResponseEntity.created(location).body(tagResponse);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TagResponse>> listAll() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid TagRequest request
    ) {
        return ResponseEntity.ok(tagService.update(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/exists/{id}")
    public ResponseEntity<TagExistsResponse> exists(@PathVariable Long id) {
        TagExistsResponse response = tagService.exists(id);
        return ResponseEntity.ok(response);
    }


    @Override
    @GetMapping("{id}/words")
    public ResponseEntity<TagWithWordsResponse> getWords(@PathVariable Long id) {
        TagWithWordsResponse response = tagService.findWordsByTag(id);
        return ResponseEntity.ok(response);
    }
}
