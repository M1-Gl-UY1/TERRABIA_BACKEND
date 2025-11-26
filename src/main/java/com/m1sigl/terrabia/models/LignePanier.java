package com.m1sigl.terrabia.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contenir") // Table representing items in a shopping cart
@Data @NoArgsConstructor
public class LignePanier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_panier")
    private Panier panier;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;

    private Integer quantite;
}