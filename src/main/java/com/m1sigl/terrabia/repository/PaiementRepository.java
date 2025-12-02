package com.m1sigl.terrabia.repository;

import com.m1sigl.terrabia.models.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement,Long> {
    Optional <Paiement> findByreferenceTransaction(String referenceTransaction);
}
