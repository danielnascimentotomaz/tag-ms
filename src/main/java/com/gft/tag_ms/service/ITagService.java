package com.gft.tag_ms.service;

import com.gft.tag_ms.dto.TagExistsResponse;
import com.gft.tag_ms.dto.TagRequest;
import com.gft.tag_ms.dto.TagResponse;
import com.gft.tag_ms.dto.TagWithWordsResponse;

import java.util.List;

public interface ITagService {

    TagResponse create(TagRequest request);
    TagResponse findById(Long id);

    List<TagResponse> findAll();

    TagResponse update(Long id, TagRequest request);

    void delete(Long id);

    TagExistsResponse exists(Long id);

    TagWithWordsResponse findWordsByTag(Long id);
}
