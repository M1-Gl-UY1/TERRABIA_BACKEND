package com.m1sigl.terrabia.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.enums.StatutCommande;
import com.m1sigl.terrabia.models.Commande;
import com.m1sigl.terrabia.models.LigneCommande;
import com.m1sigl.terrabia.models.LignePanier;
import com.m1sigl.terrabia.models.Panier;
import com.m1sigl.terrabia.repository.CommandeRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final PanierService panierService;
    

    @Transactional
    public Commande passerCommande(Long idAcheteur) {
        Panier panier = panierService.getPanierActif(idAcheteur);

        if (panier.getArticles().isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        // Création de la commande
        Commande commande = new Commande();
        commande.setAcheteur(panier.getAcheteur());
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut(StatutCommande.EN_ATTENTE);
        commande.setDetails(new ArrayList());

        double total = 0.0;

        // Transformation LignePanier -> LigneCommande
        for (LignePanier lp : panier.getArticles()) {
            LigneCommande lc = new LigneCommande();
            lc.setCommande(commande);
            lc.setProduit(lp.getProduit());
            lc.setQuantite(lp.getQuantite());
            lc.setPrixUnitaire(lp.getProduit().getPrix()); // On fige le prix
            
            total += (lc.getPrixUnitaire() * lc.getQuantite());
            commande.getDetails().add(lc);
        }

        commande.setMontantTotal(total);

        // Sauvegarde commande
        Commande savedCommande = commandeRepository.save(commande);

        // Vider le panier ou le passer en statut "ARCHIVÉ"
        panier.getArticles().clear(); 
        // Option simple : on vide les lignes
        // Ou : panier.setStatut("ARCHIVÉ"); panierRepository.save(panier);
        
        return savedCommande;
    }

    public List<Commande> getHistoriqueCommandes(Long idAcheteur) {
        return commandeRepository.findByAcheteur_IdUser(idAcheteur);
    }
}