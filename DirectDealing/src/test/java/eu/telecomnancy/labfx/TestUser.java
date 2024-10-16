/* package eu.telecomnancy.labfx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUser {

    private User user;
    private User user2;

    @BeforeEach
    void setUp() {
        // Initialisation avec un utilisateur existant dans la base de données de test
        user = new User("maxence.bouchadel@gmail.com"); // Remplacez par un email valide pour le test
    }
    @Order(1)
    @Test
    @DisplayName("Vérifier les données chargées de la base de données")
    void testLoadUserFromDB() {
        System.out.println("Chargement des données de l'utilisateur maxence.bouchadel@gmail.com");
        System.out.println("Prenom: " + user.getPrenom() + " \nNom: " + user.getNom() + " \nPseudo: " + user.getPseudo() + " \nMail: " + user.getMail() + " \nPhone: " + user.getPhone() + " \nPhoto de profil: " + user.getPhotoProfil() + " \nLocalisation: " + user.getLocalisation() + " \nDate d'inscription: " + user.getDateInscription() + " \nStatus du compte: " + user.getStatusCompte() + " \nEtat du compte: " + user.getEtatCompte() + " \nNombre de florain: " + user.getNbFlorain() + " \nHistorique florain: " + user.getHistoriqueFlorain() + " \nNote: " + user.getNote());
        assertEquals("Maxence", user.getPrenom(), "Le prénom doit être chargé depuis la base de données");
        assertEquals("Bouchadel", user.getNom(), "Le nom doit être chargé depuis la base de données");
        assertEquals("Maxencedu66", user.getPseudo(), "Le pseudo doit être chargé depuis la base de données");
        assertEquals("Le Teil", user.getLocalisation(), "La localisation doit être chargée depuis la base de données");
        assertEquals("/Users/maxence/Downloads/chat.png", user.getPhotoProfil(), "La photo de profil doit être chargée depuis la base de données");
        assertEquals("0646691077", user.getPhone(), "Le téléphone doit être chargé depuis la base de données");
        assertEquals(LocalDate.of(2024, 1, 8), user.getDateInscription(), "La date d'inscription doit être chargée depuis la base de données");
        assertEquals(100, user.getNbFlorain(), "Le nombre de florain doit être chargé depuis la base de données");
    }
    @Order(2)
    @Test
    @DisplayName("Vérifier la mise à jour des données utilisateur")
    void testUpdateUser() {
        String nouveauPrenom = "Alice";
        String nouveauNom = "Doe";

        user.setPrenom(nouveauPrenom);
        user.setNom(nouveauNom);
        user.update();

        // Recharger les données pour s'assurer que la mise à jour est effective
        user2 = new User("maxence.bouchadel@gmail.com");
        //print prenom nom
        System.out.println("Mise à jour des données utilisateur");
        System.out.println("Prenom: " + user2.getPrenom() + " \n Nom: " + user2.getNom() + " \nPseudo: " + user2.getPseudo() + " \nMail: " + user2.getMail() + " \nPhone: " + user2.getPhone() + " \nPhoto de profil: " + user2.getPhotoProfil() + " \nLocalisation: " + user2.getLocalisation() + " \nDate d'inscription: " + user2.getDateInscription() + " \nStatus du compte: " + user2.getStatusCompte() + " \nEtat du compte: " + user2.getEtatCompte() + " \nNombre de florain: " + user2.getNbFlorain() + " \nHistorique florain: " + user2.getHistoriqueFlorain() + " \nNote: " + user2.getNote());
        assertEquals(nouveauPrenom, user.getPrenom(), "Le prénom doit être mis à jour");
        assertEquals(nouveauNom, user.getNom(), "Le nom doit être mis à jour");


        // Remettre les données à leur valeur initiale
        user2.setPrenom("Maxence");
        user2.setNom("Bouchadel");
        user2.update();
        
    }

}
 */