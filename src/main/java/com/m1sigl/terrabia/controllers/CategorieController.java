package com.m1sigl.terrabia.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.models.Categorie;
import com.m1sigl.terrabia.repository.CategorieRepository;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    // Endpoint 2.1 : Liste des catégories (Public)
    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories() {
        return ResponseEntity.ok(categorieRepository.findAll());
    }

     @PostMapping
    // On autorise seulement les VENDEURS (ou ADMIN si tu as créé ce rôle) à ajouter des catégories
    @PreAuthorize("hasRole('VENDEUR')")
    public ResponseEntity<Categorie> createCategorie(@RequestBody @jakarta.validation.Valid Categorie categorie) {
        return ResponseEntity.ok(categorieRepository.save(categorie));
    }
}