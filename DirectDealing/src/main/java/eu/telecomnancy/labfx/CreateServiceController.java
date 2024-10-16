package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;


public class CreateServiceController {



    private SkeletonController skeleton_controller;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    private User currentUser;


    @FXML private TextField title;
    @FXML private TextField description;
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private TextField time;
    @FXML private TextField price;

    @FXML
    public void initialize() {
        System.out.println("ServiceController initialize");
        currentUser = Main.getCurrentUser();
        // Configurez le DatePicker pour début, afin de bloquer les dates des jours passés
        start.setDayCellFactory(disablePastDates());
        // Configurez le DatePicker pour fin
        end.setDayCellFactory(disablePastDates());
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

    @FXML public void handleCreateOffer() {
            System.out.println("Create service");

            String titleField = title.getText();
            String descriptionField = description.getText();
            String priceStr = price.getText();
            
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
            if (start.getValue() == null) {
                skeleton_controller.flash("Veuillez remplir la date de début", "red");
                return;
            }
            if (end.getValue() == null) {
                skeleton_controller.flash("Veuillez remplir la date de fin", "red");
                return;
            }
            if (time.getText().isEmpty()) {
                skeleton_controller.flash("Veuillez remplir l'heure", "red");
                return;
            }
            

            
            try{
                int price = Integer.parseInt(priceStr);

                LocalDate startField = start.getValue();
                LocalDate endField = end.getValue();
                LocalTime time = LocalTime.parse(this.time.getText()); //transform textfield to local time
                
                if (startField == null || endField == null || time == null) {
                    System.out.println("Veuillez remplir tous les champs requis.");
                    skeleton_controller.flash("Veuillez remplir tous les champs requis.", "red");
                    return;
                }
                
                ServiceOffer newOffer = new ServiceOffer(
                    currentUser.getMail(), 
                    titleField, 
                    descriptionField, 
                    startField,
                    endField, 
                    time,
                    price
                );
                System.out.println("Offre de service bien créé");
                //print info newoffer
                //System.out.println("id: " + newOffer.getId() + " title: " + newOffer.getTitle() + " description: " + newOffer.getDescription() + " start: " + newOffer.getStart() + " end: " + newOffer.getEnd() + " time: " + newOffer.getTime() + " price: " + newOffer.getPrice() + " nb recurrence: " + newOffer.getRecurrency() + " supplier mail: " + newOffer.getSupplierMail() + " est pris: " + newOffer.getEstPris());
                skeleton_controller.loadServiceOfferPage(newOffer);

            } catch (Exception e) {
                System.out.println("Veuillez remplir tous les champs requis. Recommencez !");
                skeleton_controller.flash("Veuillez remplir tous les champs requis. Recommencez !", "red");
                System.err.println(e);
                return;
            }
        }

    @FXML public void cancel() {
        System.out.println("cancel");
        skeleton_controller.loadListServiceOfferPage();
    }
}
