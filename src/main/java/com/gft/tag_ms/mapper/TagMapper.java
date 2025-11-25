package com.gft.tag_ms.mapper;

import com.gft.tag_ms.dto.TagRequest;
import com.gft.tag_ms.dto.TagResponse;
import com.gft.tag_ms.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "id", ignore = true)
    Tag toEntity(TagRequest tagRequest);


    TagResponse toResponse(Tag entity);





}
