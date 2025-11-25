package com.gft.tag_ms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "TagRequest",
        description = "Objeto utilizado para criar uma nova tag no sistema."
)
public record TagRequest(
        @Schema(
                description = "Nome da etiqueta (tag) a ser criada.",
                example = "Promoção"
        )
        @NotBlank(message = "O nome da etiqueta e obrigatorio")
        String name
) {

}
