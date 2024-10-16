package eu.telecomnancy.labfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import java.util.ArrayList;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SkeletonController {

    public MainController main_controller;
    private User currentUser;
    private String supplierForMessaging;
    private String message;

    public void setMainController(MainController main_controller) {
        this.main_controller = main_controller;
    }
    public void setSupplierForMessaging(String pseudo) {
        this.supplierForMessaging = pseudo;
    }

    public void setSupplierForMessaging(String pseudo, String message) {
        this.supplierForMessaging = pseudo;
        this.message = message;
    }

    @FXML private SplitPane skeletonContent;
    @FXML private VBox menuContent;
    @FXML private ProfileController profil_controller;
    @FXML private VBox profileContent;
    @FXML private VBox mainContent;
    @FXML private HBox flashMessageContainer;
    @FXML private Label flashMessageLabel;




    public void initialize(){
        System.out.println("Initialisation de la session");
        currentUser = Main.getCurrentUser();
    }

    public void setProfileController(ProfileController profil_controller){
        this.profil_controller = profil_controller;
    }


    // Fonction qui d'afficher un message flash
    public void flash(String message, String color) {
        flashMessageLabel.setText(message);
        flashMessageContainer.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 20;");
        flashMessageContainer.setVisible(true);
        // Temporisateur pour masquer le message flash après 5 secondes
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> closeFlashMessage()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void closeFlashMessage() {
        flashMessageContainer.setVisible(false);
    }

    // Vous pouvez également ajouter des méthodes spécifiques pour charger le menu et le profil si nécessaire
    public void loadMenuPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Menu.fxml"));
            Parent menu_page = loader.load();

            MenuController menu_controller = loader.getController();
            menu_controller.setSkeletonController(this);

            menuContent.getChildren().setAll(menu_page);
            Main.applyCursorChangeToScene(menuContent);
            closeFlashMessage();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProfilePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Profile.fxml"));
            Parent profil_page = loader.load();

            ProfileController profile_controller = loader.getController();
            profile_controller.setSkeletonController(this);
            setProfileController(profile_controller);
            profileContent.getChildren().setAll(profil_page);
            Main.applyCursorChangeToScene(profileContent);
            closeFlashMessage();
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Fonction qui permet de charger la home page:
    public void loadHomePage(){
        try {
            System.out.println("Chargement de la page Home");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/HomePage.fxml"));
            Parent home = loader.load();

            HomeController home_controller = loader.getController();
            home_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(home);
            Main.applyCursorChangeToScene(mainContent);
            closeFlashMessage();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page de création des équipements
    public void loadCreateEquipmentPage(){
        try {
            System.out.println("Chargement de la page de création d'une offre d'équipement");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/CreateEquipment.fxml"));
            Parent create_equipment = loader.load();

            CreateEquipmentController create_equipment_controller = loader.getController();
            create_equipment_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(create_equipment);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadEquipmentOfferPage(EquipmentOffer offer){
        try {
            System.out.println("Chargement de la page d'une offre d'équipement");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/EquipmentOffer.fxml"));
            Parent equipment_offer = loader.load();

            EquipmentOfferController equipment_offer_controller = loader.getController();
            equipment_offer_controller.setSkeletonController(this);
            equipment_offer_controller.setCurrentOffer(offer);


            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(equipment_offer);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page de liste des offres d'équipement
    public void loadListEquipmentOfferPage(){
        try {
            System.out.println("Chargement de la page de liste des offres d'équipement");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ConsultEquipment.fxml"));
            Parent list_equipment_offer = loader.load();

            ConsultEquipmentController list_equipment_offer_controller = loader.getController();
            list_equipment_offer_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(list_equipment_offer);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page de liste des offres de service
    public void loadListServiceOfferPage(){
        try {
            System.out.println("Chargement de la page de liste des offres de service");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ConsultService.fxml"));
            Parent list_service_offer = loader.load();

            ConsultServiceController list_service_offer_controller = loader.getController();
            list_service_offer_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(list_service_offer);
            Main.applyCursorChangeToScene(mainContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Fonction qui permet de charger la page de profile privé
    public void loadPrivateProfile() {
        try {
            System.out.println("Chargement de la page de profile privé");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/PrivateProfilePage.fxml"));
            Parent privateProfile = loader.load();

            PrivateProfileController privateProfile_controller = loader.getController();
            privateProfile_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(privateProfile);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page d'affichage d'une offre de service
    public void loadServiceOfferPage(ServiceOffer offer){
        try {
            System.out.println("Chargement de la page d'une offre de service");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ServiceOffer.fxml"));
            Parent service_offer = loader.load();
            ServiceOfferController service_offer_controller = loader.getController();
            service_offer_controller.setSkeletonController(this);
            service_offer_controller.setCurrentOffer(offer);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(service_offer);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page de création d'une offre de service
    public void loadCreateServicePage(){
        try {
            System.out.println("Chargement de la page de création d'une offre de service");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/CreateService.fxml"));
            Parent create_service = loader.load();

            CreateServiceController create_service_controller = loader.getController();
            create_service_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(create_service);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page de la carte
    public void loadMapPage(){
        try {
            System.out.println("Chargement de la Map page");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Map.fxml"));
            Parent map = loader.load();

            MapController map_controller = loader.getController();
            map_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(map);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page du planning
    public void loadPlanningPage(){
        try {
            System.out.println("Chargement de la page planning");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Planning.fxml"));
            Parent planning = loader.load();

            PlanningController planning_controller = loader.getController();
            planning_controller.setSkeletonController(this);

            // Ajouter la page d'inscription à la scène
            mainContent.getChildren().setAll(planning);
            Main.applyCursorChangeToScene(mainContent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction qui permet de charger la page de la messagerie
    public void loadMessageriePage() {
        try {
            System.out.println("Chargement de la page messagerie");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Messagerie.fxml"));
            Parent messagerie = loader.load();
    
            MessagerieController messagerie_controller = loader.getController();
            messagerie_controller.setSkeletonController(this);
    
            // Passer le pseudo du fournisseur à MessagerieController
            messagerie_controller.setInitialContact(supplierForMessaging);
            messagerie_controller.setInitialMessage(message);
            messagerie_controller.initializeListContact();
            supplierForMessaging = null; // Réinitialiser la variable après utilisation
            message = null;
    
            mainContent.getChildren().setAll(messagerie);
            Main.applyCursorChangeToScene(mainContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateProfile(){
        profil_controller.updateProfileInfo(currentUser);
    }

    
}


