package com.m1sigl.terrabia.dto;

import com.m1sigl.terrabia.enums.Role_;
import com.m1sigl.terrabia.enums.Sexe;

import lombok.Data;

@Data
public class RegisterDto {
    // Champs communs
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String numTel;
    private String ville;
    private Sexe sexe;
    private Role_ role; // VENDEUR ou ACHETEUR

    // Spécifique Vendeur
    private String numeroCni;
}