package com.m1sigl.terrabia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.models.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}