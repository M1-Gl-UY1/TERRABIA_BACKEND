package com.m1sigl.terrabia.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m1sigl.terrabia.dto.LoginDto;
import com.m1sigl.terrabia.dto.RegisterDto;
import com.m1sigl.terrabia.enums.Role_;
import com.m1sigl.terrabia.models.Acheteur;
import com.m1sigl.terrabia.models.Utilisateur;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.AcheteurRepository;
import com.m1sigl.terrabia.repository.UtilisateurRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;
import com.m1sigl.terrabia.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*") // Autorise le Frontend
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final VendeurRepository vendeurRepository;
    private final AcheteurRepository acheteurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    // --- INSCRIPTION ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        if (utilisateurRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Erreur: Email déjà utilisé !");
        }

        if (dto.getRole() == Role_.VENDEUR) {
            Vendeur vendeur = new Vendeur();
            mapCommonFields(vendeur, dto);
            vendeur.setNumeroCni(dto.getNumeroCni()); // Spécifique Vendeur
            vendeurRepository.save(vendeur);
            return ResponseEntity.ok("Vendeur inscrit avec succès !");
        } else {
            Acheteur acheteur = new Acheteur();
            mapCommonFields(acheteur, dto);
            // On pourrait initialiser le panier ici si besoin
            acheteurRepository.save(acheteur);
            return ResponseEntity.ok("Acheteur inscrit avec succès !");
        }
    }

    // --- CONNEXION ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        // Génération du Token (valide 30 jours)
        final String token = jwtUtils.generateToken(userDetails);
        
        // Récupérer l'utilisateur pour renvoyer son rôle et son ID au frontend
        Utilisateur user = utilisateurRepository.findByEmail(dto.getEmail()).orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("idUser", user.getIdUser());
        response.put("nom", user.getNom());

        return ResponseEntity.ok(response);
    }

    // Méthode utilitaire pour éviter la duplication de code
    private void mapCommonFields(Utilisateur user, RegisterDto dto) {
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // HASH DU MDP
        user.setNumTel(dto.getNumTel());
        user.setVille(dto.getVille());
        user.setSexe(dto.getSexe());
        user.setRole(dto.getRole());
    }
}