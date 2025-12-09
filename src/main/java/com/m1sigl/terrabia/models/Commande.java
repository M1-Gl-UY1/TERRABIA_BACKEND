package com.m1sigl.terrabia.models;

import java.time.LocalDateTime;
import java.util.List;

import com.m1sigl.terrabia.enums.StatutCommande;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "commande")
@Data
@Setter
@NoArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommande;

    private LocalDateTime dateCommande;
    private Double montantTotal;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @ManyToOne
    @JoinColumn(name = "id_user_acheteur")
    private Acheteur acheteur;

    @ManyToOne
    @JoinColumn(name = "id_agence")
    private AgenceLivraison agenceLivraison;

    // Représente la table "constituer"
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<LigneCommande> details;
}
