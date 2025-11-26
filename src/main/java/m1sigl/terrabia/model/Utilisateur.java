package m1sigl.terrabia.model;

import jakarta.persistence.*;
import lombok.Data;
import m1sigl.terrabia.model.enumeration.Sexe;

@Entity
@Data
@Inheritance(strategy=InheritanceType.JOINED)
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    private String nom;
    private String prenom;
    private String statut;
    private String email;
    private String numeroTelephone;
    
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    private String ville;
}