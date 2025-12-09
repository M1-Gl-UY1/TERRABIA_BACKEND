package com.m1sigl.terrabia;

import com.m1sigl.terrabia.models.Categorie;
import com.m1sigl.terrabia.models.Produit;
import com.m1sigl.terrabia.models.Vendeur;
import com.m1sigl.terrabia.repository.CategorieRepository;
import com.m1sigl.terrabia.repository.VendeurRepository;
import com.m1sigl.terrabia.services.FileStorageService;
import com.m1sigl.terrabia.services.gestion_panier_commande.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // On "Mock" (simule) les services pour ne pas écrire réellement sur le disque ou la BDD
    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private VendeurRepository vendeurRepository;

    @MockBean
    private CategorieRepository categorieRepository;

    @MockBean
    private ProduitService produitService;

    @BeforeEach
    void setUp() {
        // Simulation du comportement du FileStorageService
        // Quand on lui donne un fichier, il retourne un faux nom "test-image.jpg"
        when(fileStorageService.storeFile(any())).thenReturn("test-image.jpg");
    }

    // --- SCÉNARIO 1 : SUCCÈS (Vendeur ajoute un produit) ---
    @Test
    @WithMockUser(username = "jean@vendeur.com", roles = "VENDEUR") // Simule un Vendeur connecté
    public void testAjouterProduit_Success() throws Exception {
        
        // 1. Préparer les données simulées (Mocks)
        Vendeur vendeurMock = new Vendeur();
        vendeurMock.setEmail("jean@vendeur.com");
        
        Categorie categorieMock = new Categorie();
        categorieMock.setIdCat(1L);

        // Dire aux repositories de retourner nos faux objets
        when(vendeurRepository.findByEmail("jean@vendeur.com")).thenReturn(Optional.of(vendeurMock));
        when(categorieRepository.findById(1L)).thenReturn(Optional.of(categorieMock));
        when(produitService.ajouterProduit(any(Produit.class))).thenReturn(new Produit());

        // 2. Créer le faux fichier image
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",               // nom du paramètre
                "tomate.jpg",          // nom du fichier
                "image/jpeg",          // type mime
                "contenu_fake_image".getBytes() // contenu binaire
        );

        // 3. Exécuter la requête POST Multipart
        mockMvc.perform(multipart("/api/produits")
                .file(imageFile)
                .param("nom", "Tomates Bio")
                .param("prix", "12.99")
                .param("quantite", "50")
                .param("description", "Délicieuses tomates")
                .param("idCategorie", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk()); // On s'attend à un code 200 OK
    }

    // --- SCÉNARIO 2 : ÉCHEC (Un Acheteur essaie d'ajouter) ---
    @Test
    @WithMockUser(username = "client@test.com", roles = "ACHETEUR") // Simule un Acheteur
    public void testAjouterProduit_Forbidden_For_Acheteur() throws Exception {
        
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());

        mockMvc.perform(multipart("/api/produits")
                .file(imageFile)
                .param("nom", "Test")
                .param("prix", "10")
                .param("quantite", "10")
                .param("description", "Desc")
                .param("idCategorie", "1"))
                .andExpect(status().isForbidden()); // On s'attend à 403 Forbidden
    }

    // --- SCÉNARIO 3 : ÉCHEC (Non connecté) ---
    @Test
    public void testAjouterProduit_Unauthorized_If_Not_Login() throws Exception {
        
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "data".getBytes());

        // Pas d'annotation @WithMockUser ici
        mockMvc.perform(multipart("/api/produits")
                .file(imageFile)
                .param("nom", "Test")
                .param("prix", "10")
                .param("quantite", "10")
                .param("description", "Desc")
                .param("idCategorie", "1"))
                .andExpect(status().isForbidden()); // Spring Security bloque par défaut (403)
    }
}
