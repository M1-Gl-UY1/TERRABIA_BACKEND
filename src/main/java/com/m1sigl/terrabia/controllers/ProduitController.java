package com.m1sigl.terrabia.controllers;

import com.m1sigl.terrabia.models.Categorie;
import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.CategorieRepository;
import com.m1sigl.terrabia.repository.ProduitRepository;
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
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProduitController {

    private final ProduitService produitService;
    private final FileStorageService fileStorageService;
    private final VendeurRepository vendeurRepository;
    private final CategorieRepository categorieRepository;
    private final ProduitRepository produitRepository;

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
    // Endpoint 3.1 : Liste de tous les produits (Public)
    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        // Note: Pour la pagination (page, size), on pourrait utiliser Pageable de Spring Data
        return ResponseEntity.ok(produitService.getAllProduits());
    }

    // Endpoint 3.3 : Détails d'un produit (Public)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduitById(@PathVariable Long id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint 7.1 : Produits d'un vendeur (Public ou Auth)
    @GetMapping("/vendeur/{idVendeur}")
    public ResponseEntity<List<Produit>> getProduitsByVendeur(@PathVariable Long idVendeur) {
        return ResponseEntity.ok(produitService.getProduitsParVendeur(idVendeur));
    }

    // Endpoint 3.5 : Modifier un produit (Vendeur)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('VENDEUR')")
    public ResponseEntity<?> updateProduit(
            @PathVariable Long id,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "prix", required = false) Double prix,
            @RequestParam(value = "quantite", required = false) Integer quantite,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal
    ) {
        // 1. Vérifier que le produit appartient bien au vendeur connecté
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        
        String emailVendeur = principal.getName();
        if (!produit.getVendeur().getEmail().equals(emailVendeur)) {
            return ResponseEntity.status(403).body("Ce produit ne vous appartient pas");
        }

        // 2. Mise à jour des champs (si non null)
        if (nom != null) produit.setNom(nom);
        if (prix != null) produit.setPrix(prix);
        if (quantite != null) produit.setQuantite(quantite);
        if (description != null) produit.setDescription(description);

        // 3. Gestion de la nouvelle image (si envoyée)
        if (image != null && !image.isEmpty()) {
            String fileName = fileStorageService.storeFile(image);
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();
            produit.setPhotoUrl(fileUrl);
        }

        return ResponseEntity.ok(produitRepository.save(produit));
    }
}