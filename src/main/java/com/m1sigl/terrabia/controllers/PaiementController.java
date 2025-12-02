package com.m1sigl.terrabia.controllers;

import com.m1sigl.terrabia.dto.InitPaiementDto;
import com.m1sigl.terrabia.models.Paiement;
import com.m1sigl.terrabia.services.paiement.PaiementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paiement")
@CrossOrigin("*")
public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    // POST http://localhost:8080/api/paiement/payer
    @PostMapping("/payer")
    public ResponseEntity<Paiement> payer(@RequestBody @Valid InitPaiementDto dto) {
        Paiement resultat = paiementService.initierPaiement(dto);
        return ResponseEntity.ok(resultat);
    }
}