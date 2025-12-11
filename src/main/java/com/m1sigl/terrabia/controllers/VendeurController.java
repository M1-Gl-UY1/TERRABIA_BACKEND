package com.m1sigl.terrabia.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.models.Commande;
import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.repository.CommandeRepository;
import com.m1sigl.terrabia.repository.ProduitRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendeur")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VendeurController {

    private final VendeurRepository vendeurRepository;
    private final ProduitRepository produitRepository;
    private final CommandeRepository commandeRepository;

    // Endpoint 7.3 : Profil vendeur
    @GetMapping("/{id}")
    public ResponseEntity<?> getVendeurProfil(@PathVariable Long id) {
        return vendeurRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint 7.2 : Statistiques (CRITIQUE)
    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('VENDEUR')")
    public ResponseEntity<?> getVendeurStats(@PathVariable Long id) {
        
        // 1. Récupérer les données brutes
        List<Produit> produits = produitRepository.findByVendeur_IdUser(id);
        List<Commande> commandes = commandeRepository.findCommandesByVendeurId(id);

        // 2. Calculer les stats
        int totalProducts = produits.size();
        int totalOrders = commandes.size();
        
        // Calcul du revenu (Approximatif : somme des lignes de commande concernant ce vendeur)
        // Note: Idéalement, faire cela en SQL/JPQL pour la performance
        double totalRevenue = commandes.stream()
                .flatMap(c -> c.getDetails().stream())
                .filter(d -> d.getProduit().getVendeur().getIdUser().equals(id))
                .mapToDouble(d -> d.getPrixUnitaire() * d.getQuantite())
                .sum();

        // 3. Construire la réponse JSON
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", totalProducts);
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        // Tu peux ajouter d'autres stats ici (note moyenne, etc.)

        return ResponseEntity.ok(stats);
    }
}
