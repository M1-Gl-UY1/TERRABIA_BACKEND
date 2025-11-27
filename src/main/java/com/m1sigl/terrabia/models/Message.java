package com.m1sigl.terrabia.models;
import java.time.LocalDateTime;

import com.m1sigl.terrabia.enums.StatutMessage;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "message")
@Data @NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMessage;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateEnvoi;
    private StatutMessage statut;

    @ManyToOne
    @JoinColumn(name = "id_user_emetteur")
    private Utilisateur emetteur;

    @ManyToOne
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;
}