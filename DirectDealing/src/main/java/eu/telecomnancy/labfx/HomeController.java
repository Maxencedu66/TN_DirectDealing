package eu.telecomnancy.labfx;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;

public class HomeController {
    
    private SkeletonController skeleton_controller;

    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    @FXML TableView<CombinedOffer> latest_offers;
    @FXML TableView<CombinedOffer> nearest_offers;

    private User currentUser;


    @FXML public void initialize(){
        currentUser = Main.getCurrentUser();

        // Création des colonnes pour latest_offers
        TableColumn<CombinedOffer, String> typeColumnLatest = new TableColumn<>("Type");
        TableColumn<CombinedOffer, String> userNameColumnLatest = new TableColumn<>("User Name");
        TableColumn<CombinedOffer, String> titleColumnLatest = new TableColumn<>("Title");
        TableColumn<CombinedOffer, String> priceColumnLatest = new TableColumn<>("Price");
        TableColumn<CombinedOffer, String> datePublicationColumnLatest = new TableColumn<>("Date Publication");
        TableColumn<CombinedOffer, String> descriptionColumnLatest = new TableColumn<>("Description");
        TableColumn<CombinedOffer, String> cityColumnLatest = new TableColumn<>("City");
        
        // Configuration des cellules pour latest_offers
        typeColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeString()));
        userNameColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwnerName()));
        titleColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        priceColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        datePublicationColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate_publication()));
        descriptionColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        cityColumnLatest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner().getLocalisation()));

        // Ajoute les colonnes au TableView latest_offers
        latest_offers.getColumns().add(typeColumnLatest);
        latest_offers.getColumns().add(userNameColumnLatest);
        latest_offers.getColumns().add(titleColumnLatest);
        latest_offers.getColumns().add(priceColumnLatest);
        latest_offers.getColumns().add(datePublicationColumnLatest);
        latest_offers.getColumns().add(descriptionColumnLatest);
        latest_offers.getColumns().add(cityColumnLatest);

        // Création des colonnes pour nearest_offers
        TableColumn<CombinedOffer, String> typeColumnNearest = new TableColumn<>("Type");
        TableColumn<CombinedOffer, String> userNameColumnNearest = new TableColumn<>("User Name");
        TableColumn<CombinedOffer, String> titleColumnNearest = new TableColumn<>("Title");
        TableColumn<CombinedOffer, String> priceColumnNearest = new TableColumn<>("Price");
        TableColumn<CombinedOffer, String> datePublicationColumnNearest = new TableColumn<>("Date Publication");
        TableColumn<CombinedOffer, String> descriptionColumnNearest = new TableColumn<>("Description");
        TableColumn<CombinedOffer, String> cityColumnNearest = new TableColumn<>("City");

        // Configuration des cellules pour nearest_offers
        typeColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeString()));
        userNameColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwnerName()));
        titleColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        priceColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        datePublicationColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate_publication()));
        descriptionColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        cityColumnNearest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner().getLocalisation()));

        // Ajoute les colonnes au TableView nearest_offers
        nearest_offers.getColumns().add(typeColumnNearest);
        nearest_offers.getColumns().add(userNameColumnNearest);
        nearest_offers.getColumns().add(titleColumnNearest);
        nearest_offers.getColumns().add(priceColumnNearest);
        nearest_offers.getColumns().add(datePublicationColumnNearest);
        nearest_offers.getColumns().add(descriptionColumnNearest);
        nearest_offers.getColumns().add(cityColumnNearest);

        // Ajoute les données au TableView
        ArrayList<EquipmentOffer> all_equipment = Main.getAllEquipment();
        ArrayList<ServiceOffer> all_service = Main.getAllService();
    
        ArrayList<CombinedOffer> all_offers = new ArrayList<>();
    
        // Ajouter les offres d'équipement après les avoir filtrées
        if(all_equipment != null){
            for(EquipmentOffer equipment : all_equipment){
                if(equipment.getOwner().getEtatCompte().equals("actif")){all_offers.add(new CombinedOffer(equipment));}
            }
        }
    
        // Ajouter les offres de service après les avoir filtrées
        if(all_service != null){
            for(ServiceOffer service : all_service){
                if(service.getSupplier().getEtatCompte().equals("actif")){all_offers.add(new CombinedOffer(service));}
            }
        }
    
        // Trier et afficher les offres
        // Trier par date de publication
        all_offers.sort((o1, o2) -> o2.getDate_publication().compareTo(o1.getDate_publication()));
        if(all_offers != null){
            latest_offers.setItems(FXCollections.observableArrayList(all_offers));
        }

        // Trier par distance
        all_offers.sort((o1, o2) -> {
            double distance1 = currentUser.getDistanceTo(o1.getOwner());
            double distance2 = currentUser.getDistanceTo(o2.getOwner());
                 
            return Double.compare(distance1, distance2);
        });
        if(all_offers != null){
            nearest_offers.setItems(FXCollections.observableArrayList(all_offers));
        }

        nearest_offers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !nearest_offers.getSelectionModel().isEmpty()) {
                CombinedOffer selected_item = nearest_offers.getSelectionModel().getSelectedItem();
                handleDoubleClickOnEquipment(selected_item);
            }
        });

        latest_offers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !latest_offers.getSelectionModel().isEmpty()) {
                CombinedOffer selected_item = latest_offers.getSelectionModel().getSelectedItem();
                handleDoubleClickOnEquipment(selected_item);
            }
        });
    }

    private void handleDoubleClickOnEquipment(CombinedOffer item) {
        System.out.println("Double click !!");
        // Ici, tu peux effectuer une action avec l'objet ServiceOffer sélectionné
        if(item.getTypeString().equals("Equipment")){
            EquipmentOffer equipment = new EquipmentOffer(item.getOwner().getMail(), item.getTitle(), item.getDescription(), item.getStartString(), item.getEstPris());
            skeleton_controller.loadEquipmentOfferPage(equipment);
        }else{
            ServiceOffer service = new ServiceOffer(item.getOwner().getMail(), item.getTitle(), item.getDescription(), item.getStartString(), item.getEstPris());
            skeleton_controller.loadServiceOfferPage(service);
        }
    }

    @FXML public void handleDeconnexion(){
        System.out.println("Deconnexion de la session");
        skeleton_controller.main_controller.loadWelcomePage();
    }
    
}
