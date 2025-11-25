package com.gft.tag_ms.controller;

import com.gft.tag_ms.dto.TagExistsResponse;
import com.gft.tag_ms.dto.TagRequest;
import com.gft.tag_ms.dto.TagResponse;
import com.gft.tag_ms.dto.TagWithWordsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TagControllerDoc {
    @Operation(
            summary = "Create a new Tag",
            description = "Endpoint responsável por criar uma nova tag no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tag criada com sucesso",
                    content = @Content(schema = @Schema(implementation = TagResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno no servidor"
            )
    })
    ResponseEntity<TagResponse> create(@RequestBody @Valid TagRequest request);


    @Operation(summary = "Get tag by id", description = "Returns a tag by its id.")
    public ResponseEntity<TagResponse> getById(@PathVariable Long id);


    @Operation(summary = "List all tags", description = "Returns all tags.")
    public ResponseEntity<List<TagResponse>> listAll();


    @Operation(summary = "Update a tag", description = "Updates tag data.")
    public ResponseEntity<TagResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid TagRequest request
    );




    @Operation(summary = "Delete a tag", description = "Deletes a tag by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag deleted"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id);


    @Operation(summary = "Check if tag exists", description = "Used internally by Relationship-Service.")
    public ResponseEntity<TagExistsResponse> exists(@PathVariable Long id);



    @Operation(summary = "Get words linked to a tag",
            description = "Returns words related to the tag. Service calls Relationship-Service.")
    ResponseEntity<TagWithWordsResponse> getWords(@PathVariable Long id);

}

