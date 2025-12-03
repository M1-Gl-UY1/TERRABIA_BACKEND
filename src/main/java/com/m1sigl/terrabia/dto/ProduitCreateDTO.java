package com.m1sigl.terrabia.dto;

import lombok.Data;

@Data
public class ProduitCreateDTO {
    private String nom;
    private Double prix;
    private Integer quantite;
    private String description;
    private String photoUrl;
    private Long idCategorie;
    private Long idVendeur; // L'ID du vendeur qui crée le produit
}
