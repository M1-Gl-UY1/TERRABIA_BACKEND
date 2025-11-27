package com.m1sigl.terrabia.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation")
@Data @NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConversation;

    // Liste des participants (Acheteurs, Vendeurs, Agences)
    @ManyToMany(mappedBy = "conversations")
    private List<Utilisateur> participants;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages;
}