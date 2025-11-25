package com.gft.tag_ms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TagResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
        LocalDateTime createAt

) {

}
