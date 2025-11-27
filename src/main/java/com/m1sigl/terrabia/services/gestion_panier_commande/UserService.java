package com.m1sigl.terrabia.services.gestion_panier_commande;

import java.util.Optional;

import com.m1sigl.terrabia.models.Utilisateur;
import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.models.Acheteur;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.AcheteurRepository;
import com.m1sigl.terrabia.repository.UtilisateurRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UtilisateurRepository utilisateurRepository;
    private final VendeurRepository vendeurRepository;
    private final AcheteurRepository acheteurRepository;

    // Inscription générique ou spécifique
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        // Ici, il faudrait encoder le mot de passe avec BCryptPasswordEncoder
        return utilisateurRepository.save(utilisateur);
    }
    
    public Vendeur creerVendeur(Vendeur vendeur) {
        return vendeurRepository.save(vendeur);
    }

    public Acheteur creerAcheteur(Acheteur acheteur) {
        return acheteurRepository.save(acheteur);
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
}
