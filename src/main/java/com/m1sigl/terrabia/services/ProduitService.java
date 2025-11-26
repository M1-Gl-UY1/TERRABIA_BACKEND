package com.m1sigl.terrabia.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public List<Produit> getProduitsParCategorie(Long idCat) {
        return produitRepository.findByCategorie_IdCat(idCat);
    }
    
    public List<Produit> getProduitsParVendeur(Long idVendeur) {
        return produitRepository.findByVendeur_IdUser(idVendeur);
    }

    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void supprimerProduit(Long id) {
        produitRepository.deleteById(id);
    }
}
