package com.m1sigl.terrabia.models;

import java.util.List;

import com.m1sigl.terrabia.enums.StatutPanier;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "panier")
@Data @NoArgsConstructor
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPanier;

    private StatutPanier statut;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Acheteur acheteur;

    // Représente la table "contenir"
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LignePanier> articles;
}
