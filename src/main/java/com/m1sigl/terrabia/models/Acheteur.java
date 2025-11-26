package com.m1sigl.terrabia.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acheteur")
@Data @NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id_user")
public class Acheteur extends User {

    @OneToOne(mappedBy = "acheteur", cascade = CascadeType.ALL)
    private Panier panier;

    @OneToMany(mappedBy = "acheteur")
    private List<Commande> commandes;
}