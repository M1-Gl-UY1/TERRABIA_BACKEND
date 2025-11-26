package com.m1sigl.terrabia.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.models.Conversation;
import com.m1sigl.terrabia.models.Message;
import com.m1sigl.terrabia.models.User;
import com.m1sigl.terrabia.repository.ConversationRepository;
import com.m1sigl.terrabia.repository.MessageRepository;
import com.m1sigl.terrabia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagerieService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Conversation demarrerConversation(Long idUser1, Long idUser2, String sujet) {
        User u1 = userRepository.findById(idUser1).orElseThrow();
        User u2 = userRepository.findById(idUser2).orElseThrow();

        Conversation conv = new Conversation();
        conv.setSujet(sujet);
        conv.setParticipants(Arrays.asList(u1, u2));
        
        return conversationRepository.save(conv);
    }

    public Message envoyerMessage(Long idConversation, Long idEmetteur, String contenu) {
        Conversation conv = conversationRepository.findById(idConversation).orElseThrow();
        User emetteur = userRepository.findById(idEmetteur).orElseThrow();

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
