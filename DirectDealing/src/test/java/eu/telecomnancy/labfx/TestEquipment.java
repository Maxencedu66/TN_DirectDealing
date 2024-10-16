package eu.telecomnancy.labfx;
  /* 
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEquipment {
    private EquipmentOffer offer;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("maxence@gmail.com");  
        offer = new EquipmentOffer("maxence@gmail.com");
        
    }

  
    @Order(1)
    @Test
    @DisplayName("Vérifier les informations de l'offre d'équipement")
    void testEquipmentOfferDetails() {
        assertEquals("Equipement Test", offer.getName());
        assertEquals("Description de léquipement", offer.getDescription());
        assertEquals(10, offer.getQuantity());
        assertEquals(100, offer.getPrice());
    }

    @Order(2)
    @Test
    @DisplayName("Vérifier la mise à jour des informations de l'offre d'équipement")
    void testUpdateEquipmentOffer() {
        offer.setDescription("Updated Description");
        offer.setQuantity(50);
        // Vous devez implémenter la méthode update dans EquipmentOffer
        offer.update();
        assertEquals("Updated Description", offer.getDescription());
        assertEquals(50, offer.getQuantity());

        //remettre les données à leur valeur initiale
        offer.setDescription("Description de léquipement");
        offer.setQuantity(10);
        offer.update();

    } 
    @Order(3)
    @Test
    @DisplayName("Créer une nouvelle offre d'équipement")
    void testCreateNewEquipmentOffer() {
        User testUser = new User("testuser@mail.com");
        EquipmentOffer newOffer = new EquipmentOffer(
            testUser, 
            "Nouvel Équipement", 
            "Description de l'équipement", 
            5, 
            LocalDate.now(), 
            LocalDate.now().plusDays(10), 
            200
        );
        newOffer.createNewOffer();

        // Créer une instance pour charger l'offre
        EquipmentOffer loadedOffer = new EquipmentOffer(testUser.getMail());
        loadedOffer.loadEquipmentById(newOffer.getId()); // Charger l'offre par son ID

        // Vérifier que l'offre a bien été créée
        assertEquals("Nouvel Équipement", loadedOffer.getName());
        
    }
}
*/