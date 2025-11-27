package com.m1sigl.terrabia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.enums.StatutPanier;
import com.m1sigl.terrabia.models.Panier;

@Repository 
public interface PanierRepository extends JpaRepository<Panier, Long> {
    // Trouver le panier actif d'un acheteur
    Optional<Panier> findByAcheteur_IdUserAndStatut(Long idAcheteur, StatutPanier statut);
}
