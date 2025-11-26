package com.m1sigl.terrabia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.models.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorie_IdCat(Long idCat);
    List<Produit> findByVendeur_IdUser(Long idVendeur);
    List<Produit> findByNomContainingIgnoreCase(String nom); // Recherche par mocle
}