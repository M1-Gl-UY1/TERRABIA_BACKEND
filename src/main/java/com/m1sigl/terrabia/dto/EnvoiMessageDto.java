package com.m1sigl.terrabia.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvoiMessageDto {

    @NotNull(message = "l'ID du destinataire est obligatoire")
    private Long idDestinataire;

    @NotBlank(message = "le message ne peut pas être vide")
    private String contenu;
}
