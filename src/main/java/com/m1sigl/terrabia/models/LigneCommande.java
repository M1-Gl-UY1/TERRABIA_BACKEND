package com.m1sigl.terrabia.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "constitution") 
@Data @NoArgsConstructor
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;

    private Integer quantite;
    private Double prixUnitaire; 
}
