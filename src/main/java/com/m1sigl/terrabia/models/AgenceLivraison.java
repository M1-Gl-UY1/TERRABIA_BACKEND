package com.m1sigl.terrabia.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agence_livraison")
@Data @NoArgsConstructor
public class AgenceLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;
    private String adresse;
    
}