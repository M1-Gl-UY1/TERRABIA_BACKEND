package com.m1sigl.terrabia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.enums.StatutCommande;
import com.m1sigl.terrabia.models.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByAcheteur_IdUser(Long idAcheteur);
    List<Commande> findByStatut(StatutCommande statut);
    @Query("SELECT DISTINCT c FROM Commande c JOIN c.details d WHERE d.produit.vendeur.idUser = :idVendeur")
    List<Commande> findCommandesByVendeurId(@Param("idVendeur") Long idVendeur);
}