package com.m1sigl.terrabia.controllers;

import com.m1sigl.terrabia.models.Categorie;
import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.CategorieRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;
import com.m1sigl.terrabia.services.FileStorageService;
import com.m1sigl.terrabia.services.gestion_panier_commande.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProduitController {

    private final ProduitService produitService;
    private final FileStorageService fileStorageService;
    private final VendeurRepository vendeurRepository;
    private final CategorieRepository categorieRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('VENDEUR')") // 🔒 Sécurité : Seul un vendeur passe
    public ResponseEntity<?> createProduit(
            @RequestParam("nom") String nom,
            @RequestParam("prix") Double prix,
            @RequestParam("quantite") Integer quantite,
            @RequestParam("description") String description,
            @RequestParam("idCategorie") Long idCategorie,
            @RequestParam("image") MultipartFile image, // 📷 Le fichier image
            Principal principal // 👤 L'utilisateur connecté (Token)
    ) {
        // 1. Identification du Vendeur via le Token (Sécurisé)
        String email = principal.getName();
        Vendeur vendeur = vendeurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendeur introuvable"));

        // 2. Vérification catégorie
        Categorie cat = categorieRepository.findById(idCategorie)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        // 3. Stockage de l'image
        String fileName = fileStorageService.storeFile(image);
        
        // 4. Création de l'URL complète (ex: http://localhost:8081/uploads/xyz.jpg)
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        // 5. Création et sauvegarde du produit
        Produit p = new Produit();
        p.setNom(nom);
        p.setPrix(prix);
        p.setQuantite(quantite);
        p.setDescription(description);
        p.setPhotoUrl(fileDownloadUri); // On sauvegarde l'URL
        p.setVendeur(vendeur);
        p.setCategorie(cat);

        return ResponseEntity.ok(produitService.ajouterProduit(p));
    }
}