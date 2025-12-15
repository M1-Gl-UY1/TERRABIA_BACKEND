package com.m1sigl.terrabia.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produit")
@Data @NoArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;

    private String nom;
    private Double prix;
    private Integer quantite; // Stock disponible
    private String description;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "id_cat", nullable = false)
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "id_user_vendeur", nullable = false)
    private Vendeur vendeur;
}