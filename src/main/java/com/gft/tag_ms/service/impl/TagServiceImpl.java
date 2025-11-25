package com.gft.tag_ms.service.impl;

import com.gft.tag_ms.dto.TagExistsResponse;
import com.gft.tag_ms.dto.TagRequest;
import com.gft.tag_ms.dto.TagResponse;
import com.gft.tag_ms.dto.TagWithWordsResponse;
import com.gft.tag_ms.entity.Tag;
import com.gft.tag_ms.exception.NotFoundException;
import com.gft.tag_ms.mapper.TagMapper;
import com.gft.tag_ms.repository.TagRepository;
import com.gft.tag_ms.service.ITagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {

    private  final TagRepository tagRepository;
    private  final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }


    @Override
    public TagResponse create(TagRequest request) {
        Tag entity = tagMapper.toEntity(request);
        Tag saved = tagRepository.save(entity);
        return tagMapper.toResponse(saved);
    }

    @Override
    public TagResponse findById(Long id) {
        Tag tag = tagRepository.findById(id)
                               .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id) );

        return tagMapper.toResponse(tag);
    }

    @Override
    public List<TagResponse> findAll() {

        return tagRepository.findAll()
                            .stream()
                .map( tag -> new TagResponse(tag.getId(),tag.getName(),tag.getCreateAt()))
                .collect(Collectors.toList());




    }

    @Override
    public TagResponse update(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));

        tag.setName(request.name());

        Tag updated = tagRepository.save(tag);

        return tagMapper.toResponse(updated);

    }

    @Override
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new NotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);

    }

    @Override
    public TagExistsResponse exists(Long id) {
        return new TagExistsResponse(tagRepository.existsById(id));

    }

    @Override
    public TagWithWordsResponse findWordsByTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));


        // TODO: Integrar com o Word-Service para buscar as palavras associadas a esta tag


        return new TagWithWordsResponse(tag.getId(),tag.getName(),null);
    }
}
