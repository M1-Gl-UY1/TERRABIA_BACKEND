package com.m1sigl.terrabia.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.m1sigl.terrabia.models.Acheteur;
import com.m1sigl.terrabia.models.User;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.AcheteurRepository;
import com.m1sigl.terrabia.repository.UserRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VendeurRepository vendeurRepository;
    private final AcheteurRepository acheteurRepository;

    // Inscription générique ou spécifique
    public User creerUtilisateur(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        // Ici, il faudrait encoder le mot de passe avec BCryptPasswordEncoder
        return userRepository.save(user);
    }
    
    public Vendeur creerVendeur(Vendeur vendeur) {
        return vendeurRepository.save(vendeur);
    }

    public Acheteur creerAcheteur(Acheteur acheteur) {
        return acheteurRepository.save(acheteur);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
