package com.m1sigl.terrabia.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.dto.ProduitCreateDTO;
import com.m1sigl.terrabia.models.Categorie;
import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.CategorieRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;
import com.m1sigl.terrabia.services.gestion_panier_commande.ProduitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProduitController {

    private final ProduitService produitService;
    private final CategorieRepository categorieRepository;
    private final VendeurRepository vendeurRepository;

    @GetMapping
    public List<Produit> getAll() {
        return produitService.getAllProduits();
    }
    @GetMapping("/categorie/{idCat}")
    public List<Produit> getByCategorie(@PathVariable Long idCat) {
        return produitService.getProduitsParCategorie(idCat);
    }

    @PostMapping
    public ResponseEntity<?> createProduit(@RequestBody ProduitCreateDTO dto) {
        // Mapping manuel DTO -> Entity (Idéalement utiliser MapStruct)
        Produit p = new Produit();
        p.setNom(dto.getNom());
        p.setPrix(dto.getPrix());
        p.setQuantite(dto.getQuantite());
        p.setDescription(dto.getDescription());
        p.setPhotoUrl(dto.getPhotoUrl());

        // Récupération des objets liés via les IDs du DTO
        Categorie cat = categorieRepository.findById(dto.getIdCategorie())
                .orElseThrow(() -> new RuntimeException("Catégorie inconnue"));
        Vendeur vend = vendeurRepository.findById(dto.getIdVendeur())
                .orElseThrow(() -> new RuntimeException("Vendeur inconnu"));

        p.setCategorie(cat);
        p.setVendeur(vend);

        return ResponseEntity.ok(produitService.ajouterProduit(p));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.ok().build();
    }
}
