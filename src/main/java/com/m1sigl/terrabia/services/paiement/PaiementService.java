package com.m1sigl.terrabia.services.paiement;

import com.m1sigl.terrabia.dto.InitPaiementDto;
import com.m1sigl.terrabia.enums.ModePaiement;
import com.m1sigl.terrabia.enums.StatutCommande;
import com.m1sigl.terrabia.enums.StatutPaiement;
import com.m1sigl.terrabia.models.Commande;
import com.m1sigl.terrabia.models.Paiement;
import com.m1sigl.terrabia.repository.CommandeRepository;
import com.m1sigl.terrabia.repository.PaiementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaiementService {
    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;

    public  PaiementService(PaiementRepository paiementRepository, CommandeRepository commandeRepository){
        this.paiementRepository = paiementRepository;
        this.commandeRepository = commandeRepository;
    }

    @Transactional
    public Paiement initierPaiement(InitPaiementDto dto){
//        1. Vérifier que la commande existe et n'est pas déjà payée
        Optional<Commande> commande = commandeRepository.findById(dto.getCommandeId());
        
        Double montantA_Payer = commande.get().getMontantTotal();

        // 2. Créer l'objet Paiement en base (Statut EN_ATTENTE)
        Paiement paiement = new Paiement();
        paiement.setCommandeId(dto.getCommandeId());
        paiement.setMontant(montantA_Payer);
        paiement.setModePaiement(dto.getModePaiement());
        paiement.setStatut(StatutPaiement.EN_ATTENTE);
        paiement.setDatePaiement(LocalDateTime.now());

        // Générer une référence unique temporaire
        String ref = "PAY_" + UUID.randomUUID().toString();
        paiement.setReferenceTransaction(ref);

        Paiement paiementSauvegarde = paiementRepository.save(paiement);

        // 3. Appel à l'API de paiement (SIMULATION ICI)
        boolean succesProvider = simulerAppelExterne(dto.getModePaiement());

        // 4. Mise à jour du statut selon la réponse du provider
        if (succesProvider){
            paiementSauvegarde.setStatut(StatutPaiement.VALIDE);
                commande.get().setStatut(StatutCommande.PAYEE);
        }else {
            paiementSauvegarde.setStatut(StatutPaiement.ECHOUE);
        }
        return paiementRepository.save(paiementSauvegarde);
    }

    private boolean simulerAppelExterne(ModePaiement mode) {
        System.out.println(">>> Appel vers l'API de " + mode + " en cours...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {} // On simule l'attente réseau
        return true;
    }
}
