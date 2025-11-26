package m1sigl.terrabia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPanier;

    private String statut; // ex: "EN_COURS", "VALIDE"

    @ManyToOne
    @JoinColumn(name = "id_acheteur")
    private Acheteur acheteur;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL)
    private List<Produit> produits;
}