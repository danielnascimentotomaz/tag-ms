package com.gft.tag_ms.dto;

import java.util.List;

public record TagWithWordsResponse(
        Long tagId,
        String tagName,
        List<WordDTO> words

) {
}
