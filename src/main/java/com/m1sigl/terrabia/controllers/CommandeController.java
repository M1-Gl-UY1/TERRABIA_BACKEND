package com.m1sigl.terrabia.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.models.Commande;
import com.m1sigl.terrabia.services.gestion_panier_commande.CommandeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor 
@CrossOrigin("*")
public class CommandeController {

    private final CommandeService commandeService;

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
}
