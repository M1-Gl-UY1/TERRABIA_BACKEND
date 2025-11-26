package m1sigl.terrabia.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CategorieProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorie;

    private String nomCategorie;

    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits;
}
