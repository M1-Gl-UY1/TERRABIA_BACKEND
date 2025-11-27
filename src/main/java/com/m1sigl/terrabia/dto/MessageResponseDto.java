package com.m1sigl.terrabia.dto;

import com.m1sigl.terrabia.enums.StatutMessage;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private Long idMessage;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private StatutMessage statut;
    private UtilisateurResumeDto emetteur;
    private Long idConversation;
}