package com.m1sigl.terrabia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.models.Acheteur;

@Repository
public interface AcheteurRepository extends JpaRepository<Acheteur, Long> {
}
