package com.m1sigl.terrabia.models;

import java.util.List;

import javax.management.relation.Role;

import com.m1sigl.terrabia.enums.Sexe;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_User") // Le nom dans le MLD est _User
@Inheritance(strategy = InheritanceType.JOINED)
@Data @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String nom;
    private String prenom;
    
    @Column(unique = true)
    private String email;
    
    private String password; // Indispensable pour l'auth
    private String numTel;
    
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    
    private String ville;

    @Enumerated(EnumType.STRING)
    private Role role; // Pour savoir si c'est un admin, etc.

    // Relation pour la messagerie (User participe à des conversations)
    @ManyToMany
    @JoinTable(
        name = "participer",
        joinColumns = @JoinColumn(name = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "idConversation")
    )
    private List<LignePanier> conversations;
}