package com.gft.tag_ms.service.impl;

import com.gft.tag_ms.dto.*;
import com.gft.tag_ms.entity.Tag;
import com.gft.tag_ms.exception.NotFoundException;
import com.gft.tag_ms.mapper.TagMapper;
import com.gft.tag_ms.producer.IEtiquetaExcluidaProducer;
import com.gft.tag_ms.repository.TagRepository;
import com.gft.tag_ms.service.ITagService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {

    private  final TagRepository tagRepository;
    private  final TagMapper tagMapper;
    private final IEtiquetaExcluidaProducer producer;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, IEtiquetaExcluidaProducer producer) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.producer = producer;
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


        // 1. Busca a etiqueta
        Tag tag = tagRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));


        // 2. Deleta
        tagRepository.delete(tag);


        // 3. Monta a mensagem
        EtiquetaExcluidaMensage message = new  EtiquetaExcluidaMensage(
                tag.getId(),
                LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()
        );


        // 4. Dispara evento para o RabbitMQ
        producer.notifyEtiquetaExcluida(message);


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
