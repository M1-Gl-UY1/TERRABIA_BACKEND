package m1sigl.terrabia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "idUser")
public class Acheteur extends Utilisateur{
    @OneToMany(mappedBy = "acheteur")
    private List<Commande> commandes;

    @OneToMany(mappedBy = "acheteur")
    private List<Panier> paniers;
}
