package m1sigl.terrabia.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;

    private String nom;
    private Long prix;
    private Long stock;
    private String description;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private CategorieProduit categorieProduit;

    @ManyToOne
    @JoinColumn(name = "id_vendeur")
    private Vendeur vendeur;
}
