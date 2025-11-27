package com.m1sigl.terrabia.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendeur")
@Data @NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id_user")
public class Vendeur extends Utilisateur {

    private String numeroCni;
    private Double note; // Système de notation

    @OneToMany(mappedBy = "vendeur", cascade = CascadeType.ALL)
    private List<Produit> produits;
}