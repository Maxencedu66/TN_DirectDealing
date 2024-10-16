package eu.telecomnancy.labfx;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.DateCell;

public class CreateEquipmentController {
    @FXML private TextField title;
    @FXML private TextField description;
    @FXML private TextField price;
    @FXML private TextField quantity;
    @FXML private DatePicker begin;
    @FXML private DatePicker end;
    @FXML private Button ButtonCreate;

    private User currentUser;
    private SkeletonController skeleton_controller;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    @FXML
    public void initialize() {
        System.out.println("EquipmentController initialize");
        currentUser = Main.getCurrentUser();
        // Configurez le DatePicker pour début, afin de bloquer les dates des jours passés
        begin.setDayCellFactory(disablePastDates());
        // Configurez le DatePicker pour fin
        end.setDayCellFactory(disablePastDates());
    }   

    @FXML 
    public void handleCreateOffer() {
        System.out.println("Create offer");
        String titleField = title.getText();
        String descriptionField = description.getText();
        String priceStr = price.getText();
        String quantityStr = quantity.getText();
    
        //Affichage des messages d'erreur si les champs ne sont pas remplis
        if (titleField.isEmpty()) {
            skeleton_controller.flash("Veuillez remplir le titre", "red");
            return;
        }
        if (descriptionField.isEmpty()) {
            skeleton_controller.flash("Veuillez remplir la description", "red");
            return;
        }
        if (priceStr.isEmpty()) {
            skeleton_controller.flash("Veuillez remplir le prix", "red");
            return;
        }
        if (quantityStr.isEmpty()) {
            skeleton_controller.flash("Veuillez remplir la quantité", "red");
            return;
        }
        if (begin.getValue() == null) {
            skeleton_controller.flash("Veuillez remplir la date de début", "red");
            return;
        }
        if (end.getValue() == null) {
            skeleton_controller.flash("Veuillez remplir la date de fin", "red");
            return;
        }
        if (begin.getValue().isAfter(end.getValue())) {
            skeleton_controller.flash("La date de début doit être avant la date de fin", "red");
            return;
        }
        
    
        try {
            int price = Integer.parseInt(priceStr);
            int quantity = Integer.parseInt(quantityStr);
    
            LocalDate startDate = begin.getValue(); // Peut être null
            LocalDate endDate = end.getValue(); // Peut être null
    
            EquipmentOffer newOffer = new EquipmentOffer(
                currentUser, 
                titleField, 
                descriptionField, 
                quantity, 
                startDate, 
                endDate, 
                price
                
            );
            System.out.println("Offre bien créé");
            skeleton_controller.flash("Offre correctement créé", "green");
            skeleton_controller.loadEquipmentOfferPage(newOffer);
            System.out.println("id" + newOffer.getId() + " " + newOffer.getName() + " " + newOffer.getDescription() + " " + newOffer.getQuantity() + " " + newOffer.getStartAvailability() + " " + newOffer.getEndAvailability() + " " + newOffer.getPrice());
            // L'offre est automatiquement enregistrée dans createNewOffer()
    
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format de nombre");
            skeleton_controller.flash("Erreur dans le choix de la date", "red");
        }

    }
    
    

    @FXML public void cancel() {
        skeleton_controller.loadListEquipmentOfferPage();
    }

    private Callback<DatePicker, DateCell> disablePastDates() {
        return dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Désactivez les dates antérieures à aujourd'hui
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #888888;"); // Vous pouvez changer la couleur si vous le souhaitez
                }
            }
        };
    }
}