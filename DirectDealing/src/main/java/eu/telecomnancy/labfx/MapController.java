package eu.telecomnancy.labfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;


public class MapController {

    private SkeletonController skeleton_controller;

    private Map<String, Integer> offerCountByCity = new HashMap<>();

    private User currentUser;


    public void setSkeletonController(SkeletonController skeleton_controller){
        this.skeleton_controller = skeleton_controller;
    }

    @FXML private Pane mapPane;
    private Image mapImage;
    @FXML private TableView<CombinedOffer> listOffers;

    public void initialize() {
        currentUser = Main.getCurrentUser();

        // Initialiser mapImage avec l'image de la carte
        mapImage = new Image(getClass().getResourceAsStream("/eu/telecomnancy/labfx/images/france.png"));

        drawMap();
        drawOffers();

        TableColumn<CombinedOffer, String> typeColumn = new TableColumn<>("Type");
        TableColumn<CombinedOffer, String> titleColumn = new TableColumn<>("Title");
        TableColumn<CombinedOffer, String> priceColumn = new TableColumn<>("Price");
        TableColumn<CombinedOffer, String> cityColumn = new TableColumn<>("City");
        TableColumn<CombinedOffer, String> userNameColumn = new TableColumn<>("Pseudo");

        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeString()));
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner().getLocalisation()));
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwnerName()));

        listOffers.getColumns().add(typeColumn);
        listOffers.getColumns().add(titleColumn);
        listOffers.getColumns().add(priceColumn);
        listOffers.getColumns().add(cityColumn);
        listOffers.getColumns().add(userNameColumn);

        listOffers.setItems(null); // Il faut remplacer null par une observable list contenant les offres à afficher
        // la liste doit contenir des combinedOffer
    
        listOffers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !listOffers.getSelectionModel().isEmpty()) {
                CombinedOffer selected_item = listOffers.getSelectionModel().getSelectedItem();
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

    private void drawMap() {
        // Assurez-vous que l'image a été chargée correctement
        if (mapImage != null) {
            ImageView mapImageView = new ImageView(mapImage);
            mapImageView.setFitWidth(mapPane.getWidth()); // Adaptez la taille de l'image à la taille du Pane si nécessaire
            mapImageView.setFitHeight(mapPane.getHeight()); // Adaptez la taille de l'image à la taille du Pane si nécessaire
            mapImageView.setPreserveRatio(true);
            mapPane.getChildren().add(mapImageView);
        }
    }
    

    private void drawOffers() {
        ArrayList<EquipmentOffer> allEquipmentOffers = Main.getAllEquipment();
        ArrayList<ServiceOffer> allServiceOffers = Main.getAllService();

        // Compter les offres par ville pour l'équipement
        for (EquipmentOffer offer : allEquipmentOffers) {
            String city = offer.getOwner().getLocalisation();
            offerCountByCity.put(city, offerCountByCity.getOrDefault(city, 0) + 1);
        }

        // Compter les offres par ville pour les services
        for (ServiceOffer offer : allServiceOffers) {
            String city = offer.getSupplier().getLocalisation();
            offerCountByCity.put(city, offerCountByCity.getOrDefault(city, 0) + 1);
        }

        // Dessiner les points pour toutes les offres
        for (String city : offerCountByCity.keySet()) {
            drawOfferPoint(city, offerCountByCity.get(city));
        }
    }
    
    private void drawOfferPoint(String city, int offerCount) {
        double[] gpsCoordinates = getCoordinates(city);
        double[] mapCoordinates = convertGpsToMapCoordinates(gpsCoordinates[0], gpsCoordinates[1], mapImage);
        double circleSize = calculateCircleSize(offerCount);
        Circle circle = new Circle(mapCoordinates[0], mapCoordinates[1], circleSize, Color.RED);
    
        // Changer le curseur en une petite main lorsqu'on survole un cercle
        circle.setOnMouseEntered(event -> mapPane.getScene().setCursor(Cursor.HAND));
        circle.setOnMouseExited(event -> mapPane.getScene().setCursor(Cursor.DEFAULT));
    
        // Ajout d'un gestionnaire d'événements de clic
        circle.setOnMouseClicked(event -> {
            // Mettre à jour la TableView avec les offres de la ville cliquée
            updateListOffers(city);
        });
    
        mapPane.getChildren().add(circle);
    }
    
    private void updateListOffers(String city) {
        ArrayList<CombinedOffer> offersInCity = new ArrayList<>();
    
        // Filtrez les offres d'équipement et de service par ville
        for (EquipmentOffer offer : Main.getAllEquipment()) {
            if (offer.getOwner().getLocalisation().equalsIgnoreCase(city)) {
                offersInCity.add(new CombinedOffer(offer));
            }
        }
        for (ServiceOffer offer : Main.getAllService()) {
            if (offer.getSupplier().getLocalisation().equalsIgnoreCase(city)) {
                offersInCity.add(new CombinedOffer(offer));
            }
        }
    
        // Mettez à jour la TableView avec les offres filtrées
        ObservableList<CombinedOffer> observableOffers = FXCollections.observableArrayList(offersInCity);
        listOffers.setItems(observableOffers);
    }
    
    

    private double calculateCircleSize(int offerCount) {
        // changer taille du cercle en fonction du nombre d'offres
        // par exemple, en utilisant la racine carrée du nombre d'offres
        return 5 + offerCount * 2; 
    }
    
    private double[] convertGpsToMapCoordinates(double latitude, double longitude, Image mapImage) {
        // Bornes de la carte
        double westBound = -5.0;
        double eastBound = 10.0;
        double northBound = 51.0;
        double southBound = 42.0;
    
        // Convertir la longitude en position x relative
        double relativeX = (longitude - westBound) / (eastBound - westBound);
    
        // Convertir la latitude en position y relative (inversée car la latitude diminue vers le sud)
        double relativeY = (northBound - latitude) / (northBound - southBound);
    
        // Convertir en coordonnées de l'image
        double x = relativeX * mapImage.getWidth();
        double y = relativeY * mapImage.getHeight();
    
        return new double[]{x, y};
    }

    private double[] getCoordinates(String cityName) {
        String line;
        InputStream inputStream;
        inputStream = getClass().getResourceAsStream("/eu/telecomnancy/labfx/cities.csv");
        

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                String[] cityData = line.split(",");
                if (cityData[0].equals("id")) {
                    continue; // Ignorer l'en-tête
                }
                // Vérifier si le nom de la ville correspond (en tenant compte des guillemets)
                String cityInCsv = cityData[4].replace("\"", ""); // Retirer les guillemets
                if (cityInCsv.equalsIgnoreCase(cityName)) {
                    double latitude = Double.parseDouble(cityData[6]);
                    double longitude = Double.parseDouble(cityData[7]);
                    return new double[]{latitude, longitude};
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
        return new double[]{0.0, 0.0}; // Retourner une valeur par défaut en cas d'échec
    }
}
