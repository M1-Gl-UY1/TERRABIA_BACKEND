package com.m1sigl.terrabia.services.gestion_panier_commande;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.m1sigl.terrabia.models.Utilisateur;
import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.models.Conversation;
import com.m1sigl.terrabia.models.Message;
import com.m1sigl.terrabia.repository.ConversationRepository;
import com.m1sigl.terrabia.repository.MessageRepository;
import com.m1sigl.terrabia.repository.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagerieService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;

    public Conversation demarrerConversation(Long idUser1, Long idUser2) {
        Utilisateur u1 = utilisateurRepository.findById(idUser1).orElseThrow();
        Utilisateur u2 = utilisateurRepository.findById(idUser2).orElseThrow();

        Conversation conv = new Conversation();
        conv.setParticipants(Arrays.asList(u1, u2));
        
        return conversationRepository.save(conv);
    }

    public Message envoyerMessage(Long idConversation, Long idEmetteur, String contenu) {
        Conversation conv = conversationRepository.findById(idConversation).orElseThrow();
        Utilisateur emetteur = utilisateurRepository.findById(idEmetteur).orElseThrow();

        Message msg = new Message();
        msg.setConversation(conv);
        msg.setEmetteur(emetteur);
        msg.setContenu(contenu);
        msg.setDateEnvoi(LocalDateTime.now());

        return messageRepository.save(msg);
    }

    public List<Message> getMessagesConversation(Long idConversation) {
        return messageRepository.findByConversation_IdConversationOrderByDateEnvoiAsc(idConversation);
    }
}
