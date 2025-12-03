package com.m1sigl.terrabia.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.dto.AjoutPanierDTO;
import com.m1sigl.terrabia.models.Panier;
import com.m1sigl.terrabia.services.gestion_panier_commande.PanierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/panier")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PanierController {

    private final PanierService panierService;

    @GetMapping("/{idAcheteur}")
    public ResponseEntity<Panier> getPanier(@PathVariable Long idAcheteur) {
        return ResponseEntity.ok(panierService.getPanierActif(idAcheteur));
    }

    @PostMapping("/{idAcheteur}/add")
    public ResponseEntity<Panier> addToPanier(@PathVariable Long idAcheteur, 
                                              @RequestBody AjoutPanierDTO dto) {
        Panier p = panierService.ajouterAuPanier(idAcheteur, dto.getIdProduit(), dto.getQuantite());
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/ligne/{idLignePanier}")
    public ResponseEntity<Void> deleteLigne(@PathVariable Long idLignePanier) {
        panierService.supprimerDuPanier(idLignePanier);
        return ResponseEntity.ok().build();
    }
}