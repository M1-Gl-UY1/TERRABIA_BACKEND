package com.m1sigl.terrabia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.models.Vendeur;

@Repository
public interface VendeurRepository extends JpaRepository<Vendeur, Long> {
    Optional<Vendeur> findByEmail(String email);
    
}
