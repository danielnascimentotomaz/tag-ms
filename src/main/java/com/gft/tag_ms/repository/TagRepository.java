package com.gft.tag_ms.repository;

import com.gft.tag_ms.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository  extends JpaRepository<Tag,Long> {

    boolean existsById(long id);
    boolean existsByName(String name);
}
