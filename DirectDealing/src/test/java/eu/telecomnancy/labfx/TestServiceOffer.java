package eu.telecomnancy.labfx;
 /*
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServiceOffer {
    private ServiceOffer offer;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Initialisation avec un utilisateur et une offre existante dans la base de données de test
        testUser = new User("maxence@gmail.com"); 
        offer = new ServiceOffer("maxence@gmail.com");
        System.out.println("Chargement des données de l'offre");
        System.out.println("Titre: " + offer.getTitle() + " \nDescription: " + offer.getDescription() + " \nDate: " + offer.getDate_publication() + " \nHeure: " + offer.getTime() + " \nEst récurrente: " + offer.getIsRecurrent() + " \nJours de service: " + offer.getDaysOfService() + " \nNombre de semaines de récurrence: " + offer.getRecurrency() + " \nMail du fournisseur: " + offer.getSupplierMail());
    }
    @Order(1)
    @Test
    @DisplayName("Vérifier les informations de l'offre de service")
    void testServiceOfferDetails() {
        assertEquals("Test Service", offer.getTitle());
        assertEquals("Test Description", offer.getDescription());
    }
    @Order(2)
    @Test
    @DisplayName("Vérifier la mise à jour des informations de l'offre de service")
    void testUpdateServiceOffer() {
        offer.setTitle("Updated Service");
        offer.setDescription("Updated Description");
        offer.update();

        // Recharger l'offre pour s'assurer que la mise à jour est effective
        ServiceOffer updatedOffer = new ServiceOffer(offer.getSupplierMail());

        assertEquals("Updated Service", updatedOffer.getTitle());
        assertEquals("Updated Description", updatedOffer.getDescription());

        // Remettre les données à leur valeur initiale
        offer.setTitle("Test Service");
        offer.setDescription("Test Description");
        offer.update();
    }

    @Order(3)
    @Test
    @DisplayName("Créer une nouvelle offre de service")
    void testCreateNewServiceOffer() {
        User testSupplier = new User("testsupplier@gmail.com");
        String testTitle = "New Service";
        String testDescription = "This is a new service.";
        LocalDate testDate = LocalDate.of(2024, 5, 20);
        LocalTime testTime = LocalTime.of(15, 30);
        boolean testIsRecurrent = true;
        String testDaysOfService = "2,4,6";
        int testNbRecurrencingWeeks = 3;

        ServiceOffer newOffer = new ServiceOffer(testSupplier, testTitle, testDescription, testDate, testTime, testIsRecurrent, testDaysOfService, testNbRecurrencingWeeks);

        assertEquals(testSupplier.getMail(), newOffer.getSupplierMail());
        assertEquals(testTitle, newOffer.getTitle());
        assertEquals(testDescription, newOffer.getDescription());
        assertEquals(testDate, newOffer.getDate());
        assertEquals(testTime, newOffer.getTime());
        assertEquals(testIsRecurrent, newOffer.getIsRecurrent());
        assertEquals(testDaysOfService, newOffer.getDaysOfService());
        assertEquals(testNbRecurrencingWeeks, newOffer.getRecurrency());
    }
 }*/
