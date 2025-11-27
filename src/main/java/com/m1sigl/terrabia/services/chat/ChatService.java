package com.m1sigl.terrabia.services.chat;

import com.m1sigl.terrabia.dto.EnvoiMessageDto;
import com.m1sigl.terrabia.enums.StatutMessage;
import com.m1sigl.terrabia.models.Conversation;
import com.m1sigl.terrabia.models.Message;
import com.m1sigl.terrabia.models.Utilisateur;
import com.m1sigl.terrabia.repository.ConversationRepository;
import com.m1sigl.terrabia.repository.MessageRepository;
import com.m1sigl.terrabia.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ChatService {


    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UtilisateurRepository utilisateurRepository;


    public ChatService(MessageRepository messageRepository,
                       ConversationRepository conversationRepository,
                       UtilisateurRepository utilisateurRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional
    public Message envoyerMessage(Long idExpediteur, EnvoiMessageDto dto){

        // validation des utilisateurs
        Utilisateur expediteur = utilisateurRepository.findById(idExpediteur)
                .orElseThrow(()->new RuntimeException("erreur:l'expediteur n'existe pas"));

        Utilisateur destinataire = utilisateurRepository.findById(dto.getIdDestinataire())
                .orElseThrow(()->new RuntimeException("erreur:le destinataire n'existe pas"));

        // Gestion de la conversation
        Conversation conversation = conversationRepository.findConversationEntreDeuxUsers(idExpediteur,dto.getIdDestinataire())
                .orElseGet(()->{
                    System.out.println("creation d'une nouvelle conversation entre "+expediteur.getNom()+" et "+destinataire.getNom());
                    Conversation newConv = new Conversation();

                    newConv.setParticipants(Arrays.asList(expediteur,destinataire));
                    return conversationRepository.save(newConv);
                });

        //creation du message
        Message message = new Message();
        message.setContenu(dto.getContenu());
        message.setDateEnvoi(LocalDateTime.now());
        message.setStatut(StatutMessage.ENVOYE);

        message.setEmetteur(expediteur);
        message.setConversation(conversation);

        return  messageRepository.save(message);
    }

    public List<Conversation> getMesConversations(Long idUser){
        return conversationRepository.findByParticipants_IdUser(idUser);
    }

    public List<Message> getMessagesDeConversation(Long idConversation){
        return messageRepository.findByConversation_IdConversationOrderByDateEnvoiAsc(idConversation);
    }
}