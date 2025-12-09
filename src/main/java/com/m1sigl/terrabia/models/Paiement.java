package com.m1sigl.terrabia.models;

import com.m1sigl.terrabia.enums.ModePaiement;
import com.m1sigl.terrabia.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiement;
    private Double montant;
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    // Référence unique renvoyée par l'API de paiement (ex: id de transaction Stripe ou Orange)
    private String referenceTransaction;
    private Long commandeId;
}
