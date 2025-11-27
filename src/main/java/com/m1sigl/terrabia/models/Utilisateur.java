package com.m1sigl.terrabia.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m1sigl.terrabia.enums.Role_;
import com.m1sigl.terrabia.enums.Sexe;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)
@Data @NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String nom;
    private String prenom;
    
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String numTel;
    
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    
    private String ville;

    @Enumerated(EnumType.STRING)
    private Role_ role;

    // Relation pour la messagerie (User participe à des conversations)
    @ManyToMany
    @JoinTable(
        name = "participation_conversation",
        joinColumns = @JoinColumn(name = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "idConversation")
    )
    @JsonIgnore
    private List<Conversation> conversations;
}