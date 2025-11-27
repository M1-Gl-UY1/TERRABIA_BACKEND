package com.m1sigl.terrabia.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.enums.StatutPanier;
import com.m1sigl.terrabia.models.Acheteur;
import com.m1sigl.terrabia.models.LignePanier;
import com.m1sigl.terrabia.models.Panier;
import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.repository.AcheteurRepository;
import com.m1sigl.terrabia.repository.LignePanierRepository;
import com.m1sigl.terrabia.repository.PanierRepository;
import com.m1sigl.terrabia.repository.ProduitRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PanierService {

    private final PanierRepository panierRepository;
    private final ProduitRepository produitRepository;
    private final AcheteurRepository acheteurRepository;
    private final LignePanierRepository lignePanierRepository;

    // Récupérer ou créer un panier ACTIF
    public Panier getPanierActif(Long idAcheteur) {
        return panierRepository.findByAcheteur_IdUserAndStatut(idAcheteur, StatutPanier.ACTIF)
                .orElseGet(() -> {
                    Acheteur acheteur = acheteurRepository.findById(idAcheteur)
                            .orElseThrow(() -> new RuntimeException("Acheteur introuvable"));
                    Panier nouveauPanier = new Panier();
                    nouveauPanier.setAcheteur(acheteur);
                    nouveauPanier.setStatut(null);
                    nouveauPanier.setArticles(new ArrayList<>());
                    return panierRepository.save(nouveauPanier);
                });
    }

    @Transactional
    public Panier ajouterAuPanier(Long idAcheteur, Long idProduit, int quantite) {
        Panier panier = getPanierActif(idAcheteur);
        Produit produit = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        // Vérifier si le produit est déjà dans le panier
        Optional<LignePanier> ligneExistante = panier.getArticles().stream()
                .filter(lp -> lp.getProduit().getIdProduit().equals(idProduit))
                .findFirst();

        if (ligneExistante.isPresent()) {
            LignePanier lp = ligneExistante.get();
            lp.setQuantite(lp.getQuantite() + quantite);
        } else {
            LignePanier lp = new LignePanier();
            lp.setPanier(panier);
            lp.setProduit(produit);
            lp.setQuantite(quantite);
            panier.getArticles().add(lp);
        }

        return panierRepository.save(panier);
    }
    
    @Transactional
    public void supprimerDuPanier(Long idLignePanier) {
        lignePanierRepository.deleteById(idLignePanier);
    }
}