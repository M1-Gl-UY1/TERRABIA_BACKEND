package com.m1sigl.terrabia.utils;

import com.m1sigl.terrabia.dto.MessageResponseDto;
import com.m1sigl.terrabia.dto.UtilisateurResumeDto;
import com.m1sigl.terrabia.models.Message;
import com.m1sigl.terrabia.models.Utilisateur;

public class ChatMapper {

    public static MessageResponseDto toDto(Message message) {
        if (message == null) return null;

        MessageResponseDto dto = new MessageResponseDto();
        dto.setIdMessage(message.getIdMessage());
        dto.setContenu(message.getContenu());
        dto.setDateEnvoi(message.getDateEnvoi());
        dto.setStatut(message.getStatut());
        dto.setIdConversation(message.getConversation().getIdConversation());

        // Mapping manuel de l'émetteur
        Utilisateur user = message.getEmetteur();
        UtilisateurResumeDto userDto = new UtilisateurResumeDto();
        userDto.setIdUser(user.getIdUser());
        userDto.setNom(user.getNom());
        userDto.setPrenom(user.getPrenom());

        dto.setEmetteur(userDto);

        return dto;
    }
}