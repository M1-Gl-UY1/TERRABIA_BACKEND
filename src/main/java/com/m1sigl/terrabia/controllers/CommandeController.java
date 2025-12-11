package com.m1sigl.terrabia.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.enums.StatutCommande;
import com.m1sigl.terrabia.models.Commande;
import com.m1sigl.terrabia.repository.CommandeRepository;
import com.m1sigl.terrabia.services.gestion_panier_commande.CommandeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor 
@CrossOrigin("*")
public class CommandeController {

    private final CommandeService commandeService;
    private final CommandeRepository commandeRepository;

    @PostMapping("/passer/{idAcheteur}")
    public ResponseEntity<?> passerCommande(@PathVariable Long idAcheteur) {
        try {
            Commande c = commandeService.passerCommande(idAcheteur);
            return ResponseEntity.ok(c);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/historique/{idAcheteur}")
    public List<Commande> getHistorique(@PathVariable Long idAcheteur) {
        return commandeService.getHistoriqueCommandes(idAcheteur);
    }
    // Endpoint 5.3 : Détails d'une commande
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommandeById(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint 5.4 : Commandes d'un vendeur (CRITIQUE)
    @GetMapping("/vendeur/{idVendeur}")
    @PreAuthorize("hasRole('VENDEUR')")
    public ResponseEntity<List<Commande>> getCommandesByVendeur(@PathVariable Long idVendeur, Principal principal) {
        // Sécurité : vérifier que l'ID demandé est bien celui du vendeur connecté
        // (Optionnel mais recommandé)
        
        return ResponseEntity.ok(commandeRepository.findCommandesByVendeurId(idVendeur));
    }

    // Endpoint 5.5 : Mettre à jour le statut (Vendeur)
    @PutMapping("/{id}/statut")
    @PreAuthorize("hasRole('VENDEUR')")
    public ResponseEntity<?> updateStatutCommande(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        
        String nouveauStatut = body.get("statut");
        try {
            commande.setStatut(StatutCommande.valueOf(nouveauStatut));
            commandeRepository.save(commande);
            return ResponseEntity.ok(Map.of("message", "Statut mis à jour", "statut", nouveauStatut));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Statut invalide");
        }
    }
}
