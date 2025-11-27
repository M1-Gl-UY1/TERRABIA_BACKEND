package com.m1sigl.terrabia.controllers;

import com.m1sigl.terrabia.dto.EnvoiMessageDto;
import com.m1sigl.terrabia.dto.MessageResponseDto;
import com.m1sigl.terrabia.models.Conversation;
import com.m1sigl.terrabia.models.Message;
import com.m1sigl.terrabia.models.Utilisateur;
import com.m1sigl.terrabia.repository.UtilisateurRepository;
import com.m1sigl.terrabia.services.chat.ChatService;
import com.m1sigl.terrabia.utils.ChatMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService=chatService;
    }

    /**
     * 1. ENVOYER UN MESSAGE
     * URL : POST http://localhost:8080/api/chat/send?idExpediteur=1
     * Body (JSON) : { "idDestinataire": 2, "contenu": "Salut !" }
     */
    @PostMapping("/send")
    public ResponseEntity<MessageResponseDto> envoyerMessage(
            @RequestParam Long idExpediteur,
            @RequestBody @Valid EnvoiMessageDto dto
    ) {
        Message messageBrut = chatService.envoyerMessage(idExpediteur, dto);

        // conversion
        MessageResponseDto reponse = ChatMapper.toDto(messageBrut);

        return ResponseEntity.ok(reponse);
    }

    /**
     * 2. RECUPERER MES CONVERSATIONS
     * URL : GET http://localhost:8080/api/chat/conversations?idUser=1
     */
    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getMesConversations(
            @RequestParam Long idUser){
        List<Conversation> conversations = chatService.getMesConversations(idUser);
        return ResponseEntity.ok(conversations);
    }

    /**
     * 3. RECUPERER L'HISTORIQUE D'UNE CONVERSATION
     * URL : GET http://localhost:8080/api/chat/messages/5
     * (où 5 est l'ID de la conversation)
     */
    @GetMapping("/messages/{idConversation}")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long idConversation) {
        List<Message> messagesBruts = chatService.getMessagesDeConversation(idConversation);

        // On conversion
        List<MessageResponseDto> reponsePropre = messagesBruts.stream()
                .map(ChatMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reponsePropre);
    }
}
