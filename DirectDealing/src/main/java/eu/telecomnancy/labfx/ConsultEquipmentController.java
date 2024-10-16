package eu.telecomnancy.labfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConsultEquipmentController {

    private SkeletonController skeleton_controller;
    private User currentUser;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    @FXML private TextField keywords;
    @FXML private DatePicker begin;
    @FXML private DatePicker end;
    @FXML private Slider radius;
    @FXML private TextField priceMin;
    @FXML private TextField priceMax;
    @FXML private TableView<EquipmentOffer> results;
    @FXML private Button ButtonCreate;
    @FXML private Label State;

    @FXML public void initialize() {
        // Création des colonnes
        currentUser = Main.getCurrentUser();
        TableColumn<EquipmentOffer, String> userNameColumn = new TableColumn<>("User Name");
        TableColumn<EquipmentOffer, String> titleColumn = new TableColumn<>("Title");
        TableColumn<EquipmentOffer, String> priceColumn = new TableColumn<>("Price");
        TableColumn<EquipmentOffer, String> dateStartColumn = new TableColumn<>("Start");
        TableColumn<EquipmentOffer, String> dateEndColumn = new TableColumn<>("End");
        TableColumn<EquipmentOffer, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<EquipmentOffer, String> quantityColumn = new TableColumn<>("Quantity");
        TableColumn<EquipmentOffer, String> cityColumn = new TableColumn<>("City");

        // Définir comment chaque colonne va obtenir ses valeurs
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner().getPrenom()));
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        dateStartColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartAvailabilityStr()));
        dateEndColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndAvailabilityStr()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantity())));
        cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner().getLocalisation()));

        // Ajoute les colonnes au TableView
        results.getColumns().add(userNameColumn);
        results.getColumns().add(titleColumn);
        results.getColumns().add(priceColumn);
        results.getColumns().add(dateStartColumn);
        results.getColumns().add(dateEndColumn);
        results.getColumns().add(descriptionColumn);
        results.getColumns().add(quantityColumn);
        results.getColumns().add(cityColumn);

        // Ajoute les données au TableView
        ArrayList<EquipmentOffer> all_equipment = Main.getAllEquipmentHome();
        all_equipment.removeIf(item -> item.getOwner().getEtatCompte().equals("sommeil"));

        if(all_equipment != null){
            results.setItems(FXCollections.observableArrayList(all_equipment));
        }

        results.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !results.getSelectionModel().isEmpty()) {
                EquipmentOffer selectedEquipment = results.getSelectionModel().getSelectedItem();
                handleDoubleClickOnEquipment(selectedEquipment);
            }
        });

        if(currentUser.getEtatCompte().equals("sommeil")){
            ButtonCreate.setDisable(true);
            State.setVisible(true);
        }else{
            State.setVisible(false);
        }
    }

    private void handleDoubleClickOnEquipment(EquipmentOffer equipment) {
        // Ici, tu peux effectuer une action avec l'objet ServiceOffer sélectionné
        skeleton_controller.loadEquipmentOfferPage(equipment);
    }

    @FXML public void handleCreateOffer() {
        System.out.println("Create offer");
        skeleton_controller.loadCreateEquipmentPage();
    }

    @FXML
    public void handleSearch() {
        String keywordText = keywords.getText();
        LocalDate startDate = begin.getValue();
        LocalDate endDate = end.getValue();
        Integer minPrice = null;
        Integer maxPrice = null;
        double selectedRadius = radius.getValue();
    
        try {
            if (!priceMin.getText().isEmpty()) {
                minPrice = Integer.parseInt(priceMin.getText());
            }
            if (!priceMax.getText().isEmpty()) {
                maxPrice = Integer.parseInt(priceMax.getText());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price");
            skeleton_controller.flash("Veuillez entrer un prix valide", "red");

        }
    
        List<EquipmentOffer> searchResults = EquipmentOffer.searchOffers(currentUser, keywordText, startDate, endDate, minPrice, maxPrice, selectedRadius);
    
        // Mettre à jour le TableView avec les résultats
        results.getItems().setAll(searchResults);
    }
        

    @FXML public void handleReset() {
        System.out.println("Reset");
        skeleton_controller.loadListEquipmentOfferPage();
        
    }

    @FXML public void cancel(){
        System.out.println("Go back !");
        skeleton_controller.loadHomePage();
    }
}
